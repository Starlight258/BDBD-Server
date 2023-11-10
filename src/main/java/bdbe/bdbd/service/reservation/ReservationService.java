package bdbe.bdbd.service.reservation;


import bdbe.bdbd._core.exception.BadRequestError;
import bdbe.bdbd._core.exception.ForbiddenError;
import bdbe.bdbd._core.exception.NotFoundError;
import bdbe.bdbd._core.utils.DateUtils;
import bdbe.bdbd.model.Code.DayType;
import bdbe.bdbd.model.bay.Bay;
import bdbe.bdbd.dto.reservation.ReservationRequest;
import bdbe.bdbd.dto.reservation.ReservationResponse;
import bdbe.bdbd.repository.bay.BayJPARepository;
import bdbe.bdbd.model.carwash.Carwash;
import bdbe.bdbd.repository.carwash.CarwashJPARepository;
import bdbe.bdbd.model.file.File;
import bdbe.bdbd.repository.file.FileJPARepository;
import bdbe.bdbd.model.location.Location;
import bdbe.bdbd.repository.location.LocationJPARepository;
import bdbe.bdbd.model.reservation.Reservation;
import bdbe.bdbd.model.optime.Optime;
import bdbe.bdbd.repository.optime.OptimeJPARepository;
import bdbe.bdbd.repository.reservation.ReservationJPARepository;
import bdbe.bdbd.dto.reservation.ReservationResponse.ReservationInfoDTO;
import bdbe.bdbd.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationJPARepository reservationJPARepository;
    private final CarwashJPARepository carwashJPARepository;
    private final BayJPARepository bayJPARepository;
    private final LocationJPARepository locationJPARepository;
    private final OptimeJPARepository optimeJPARepository;
    private final FileJPARepository fileJPARepository;

    @Transactional
    public Reservation save(ReservationRequest.SaveDTO dto, Long carwashId, Long bayId, Member sessionMember) {
        Carwash carwash = findCarwashById(carwashId);
        Optime optime = findOptime(carwash, dto.getStartTime());

        validateReservationTime(dto.getStartTime(), dto.getEndTime(), optime, bayId);
        Bay bay = findBayById(bayId);

        Reservation reservation = dto.toReservationEntity(carwash, bay, sessionMember);
        reservationJPARepository.save(reservation);
        return reservation;
    }

    @Transactional
    public void update(ReservationRequest.UpdateDTO dto, Long reservationId, Member member) {
        Reservation reservation = reservationJPARepository.findById(reservationId)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("ReservationId", "Reservation not found")
                ));
        if (reservation.getMember().getId() != member.getId())
            throw new ForbiddenError(
                    ForbiddenError.ErrorCode.RESOURCE_ACCESS_FORBIDDEN,
                    Collections.singletonMap("MemberId", "Member is not have permission to modify this reservation.")
            );
        // 세차장 id 확인
        Long carwashId = reservation.getBay().getCarwash().getId();
        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("CarwashId", "Carwash not found"+ carwashId )
                ));
        // 예약시간 검증
        Long bayId = reservation.getBay().getId();
        LocalDateTime startTime = dto.getStartTime();
        LocalDateTime endTime = dto.getEndTime();
        Optime optime = findOptime(carwash, startTime);
        validateReservationTime(startTime, endTime, optime, bayId);
        // 예약 갱신
        reservation.updateReservation(dto.getStartTime(), dto.getEndTime(), carwash);
    }

    @Transactional
    public void delete(Long reservationId, Member member) {
        Reservation reservation = reservationJPARepository.findById(reservationId)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("ReservationId", "Reservation not found")
                ));

        if (reservation.getMember().getId() != member.getId())
            throw new ForbiddenError(
                    ForbiddenError.ErrorCode.RESOURCE_ACCESS_FORBIDDEN,
                    Collections.singletonMap("MemberId", "Member is not have permission to modify this reservation.")
            );

        reservation.changeDeletedFlag(true);
    }


    private Carwash findCarwashById(Long id) {
        return carwashJPARepository.findById(id)
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("CarwashId", "Carwash not found")
                ));
    }

    public Optime findOptime(Carwash carwash, LocalDateTime startTime) {
        List<Optime> optimeList = optimeJPARepository.findByCarwash_Id(carwash.getId());
        DayOfWeek dayOfWeek = startTime.getDayOfWeek();
        DayType dayType = DateUtils.getDayType(dayOfWeek);
        return optimeList.stream()
                .filter(o -> o.getDayType() == dayType)
                .findFirst()
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("Optime", "Optime not found")
                ));
    }

    public void validateReservationTime(LocalDateTime startTime, LocalDateTime endTime, Optime optime, Long bayId) {
        LocalTime opStartTime = optime.getStartTime();
        LocalTime opEndTime = optime.getEndTime();
        LocalTime requestStartTimePart = startTime.toLocalTime();
        LocalTime requestEndTimePart = endTime.toLocalTime();

// 예약 시간이 30분 단위인지 확인
        if (startTime.getMinute() % 30 != 0 || endTime.getMinute() % 30 != 0) {
            throw new BadRequestError(
                    BadRequestError.ErrorCode.VALIDATION_FAILED,
                    Collections.singletonMap("time", "Reservation time must be in 30-minute increments.")
            );
        }

// 종료 시간이 시작 시간보다 최소 30분 이후인지 확인
        long minutesBetween = Duration.between(startTime, endTime).toMinutes();
        if (minutesBetween < 30) {
            throw new BadRequestError(
                    BadRequestError.ErrorCode.VALIDATION_FAILED,
                    Collections.singletonMap("duration", "Reservation duration must be at least 30 minutes.")
            );
        }

// 종료 시간과 시작 시간의 차이가 30분 단위인지 확인
        if (minutesBetween % 30 != 0) {
            throw new BadRequestError(
                    BadRequestError.ErrorCode.VALIDATION_FAILED,
                    Collections.singletonMap("duration", "Reservation duration must be a multiple of 30 minutes.")
            );
        }

// 예약이 운영시간을 넘지 않도록 함
        if (!((opStartTime.isBefore(requestStartTimePart) || opStartTime.equals(requestStartTimePart)) &&
                (opEndTime.isAfter(requestEndTimePart) || opEndTime.equals(requestEndTimePart)))) {
            throw new BadRequestError(
                    BadRequestError.ErrorCode.VALIDATION_FAILED,
                    Collections.singletonMap("operatingHours", "Reservation time is out of operating hours")
            );
        }
        // 이미 예약된 시간은 피하도록 함
        List<Reservation> reservationList = reservationJPARepository.findByBay_IdAndIsDeletedFalse(bayId);

        boolean isOverlapping = reservationList.stream()
                .anyMatch(existingReservation -> {
                    LocalDateTime existingStartTime = existingReservation.getStartTime();
                    LocalDateTime existingEndTime = existingReservation.getEndTime();
                    return !(
                            (endTime.isBefore(existingStartTime) || endTime.isEqual(existingStartTime)) ||
                                    (startTime.isAfter(existingEndTime) || startTime.isEqual(existingEndTime))
                    );
                });

        if (isOverlapping) {
            throw new BadRequestError(
                    BadRequestError.ErrorCode.DUPLICATE_RESOURCE,
                    Collections.singletonMap("Reservation time", "Reservation time overlaps with an existing reservation.")
                    );
        }

    }

    private Bay findBayById(Long id) {
        return bayJPARepository.findById(id)
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("BayId", "Bay not found")
                ));
    }


    public ReservationResponse.findAllResponseDTO findAllByCarwash(Long carwashId) {
        // 세차장 존재하는지 확인
        carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("CarwashId", "Carwash not found"+ carwashId )
                ));
        //베이에서 해당 세차장 id와 관련된 베이 객체 모두 찾기
        List<Bay> bayList = bayJPARepository.findByCarwashId(carwashId);
        // id만 추출하기
        List<Long> bayIdList = bayJPARepository.findIdsByCarwashId(carwashId);
        //예약에서 베이 id 리스트로 모두 찾기
        List<Reservation> reservationList = reservationJPARepository.findByBayIdInAndIsDeletedFalse(bayIdList);

        return new ReservationResponse.findAllResponseDTO(bayList, reservationList);
    }

    public ReservationResponse.findLatestOneResponseDTO fetchLatestReservation(Long reservationId) {
        // 가장 최근의 예약 찾기
        Reservation reservation = reservationJPARepository.findById(reservationId)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("ReservationId", "Reservation not found")
                ));
        // 예약과 관련된 베이 찾기
        Bay bay = bayJPARepository.findById(reservation.getBay().getId())
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("BayId", "Bay not found")
                ));
        // 베이가 속해있는 세차장 찾기
        Carwash carwash = carwashJPARepository.findById(bay.getCarwash().getId())
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("CarwashId", "Carwash not found")
                ));
        // 세차장이 위치한 위치 찾기
        Location location = locationJPARepository.findById(carwash.getLocation().getId())
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("LocationId", "Location not found")
                ));

        File file = fileJPARepository.findFirstByCarwashIdAndIsDeletedFalseOrderByUploadedAtAsc(carwash.getId()).orElse(null);

        return new ReservationResponse.findLatestOneResponseDTO(reservation, bay, carwash, location, file);
    }

    public ReservationResponse.fetchCurrentStatusReservationDTO fetchCurrentStatusReservation(Member sessionMember) {
        // 유저의 예약내역 모두 조회
        List<Reservation> reservationList = reservationJPARepository.findByMemberIdAndIsDeletedFalse(sessionMember.getId());
        // 현재, 다가오는, 완료된 예약 찾기
        List<ReservationInfoDTO> current = new ArrayList<>();
        List<ReservationInfoDTO> upcoming = new ArrayList<>();
        List<ReservationInfoDTO> completed = new ArrayList<>();
        // 현재 날짜, 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        // 예약 분류하기
        for (Reservation reservation : reservationList) {
            Bay bay = bayJPARepository.findById(reservation.getBay().getId())
                    .orElseThrow(() -> new NotFoundError(
                            NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                            Collections.singletonMap("BayId", "Bay not found")
                    ));
            Carwash carwash = carwashJPARepository.findById(bay.getCarwash().getId())
                    .orElseThrow(() -> new NotFoundError(
                            NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                            Collections.singletonMap("CarwashId", "Carwash not found")
                    ));

            LocalDateTime startDateTime = reservation.getStartTime();
            LocalDate reservationDate = startDateTime.toLocalDate();
            LocalDateTime endDateTime = reservation.getEndTime();

            if (reservationDate.equals(today)) {
                if (now.isAfter(startDateTime) && now.isBefore(endDateTime)) {
                    current.add(new ReservationInfoDTO(reservation, bay, carwash));
                } else if (now.isBefore(startDateTime)) {
                    upcoming.add(new ReservationInfoDTO(reservation, bay, carwash));
                } else if (now.isAfter(endDateTime)) {
                    completed.add(new ReservationInfoDTO(reservation, bay, carwash));
                }
            } else if (reservationDate.isBefore(today)) {
                completed.add(new ReservationInfoDTO(reservation, bay, carwash));
            } else if (reservationDate.isAfter(today)) {
                upcoming.add(new ReservationInfoDTO(reservation, bay, carwash));
            } else {
                throw new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("ReservationId", "Reservation not found")
                );
            }
        }
        return new ReservationResponse.fetchCurrentStatusReservationDTO(current, upcoming, completed);
    }

    public ReservationResponse.fetchRecentReservationDTO fetchRecentReservation(Member sessionMember) {
        // 유저의 예약내역 모두 조회
        Pageable pageable = PageRequest.of(0, 5); // 최대 5개까지만 가져오기
        List<Reservation> reservationList = reservationJPARepository.findByMemberIdJoinFetch(sessionMember.getId(), pageable);
        List<ReservationResponse.RecentReservation> recentReservations = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            Bay bay = bayJPARepository.findById(reservation.getBay().getId())
                    .orElseThrow(() -> new NotFoundError(
                            NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                            Collections.singletonMap("BayId", "Bay not found")
                    ));
            Carwash carwash = carwashJPARepository.findById(bay.getCarwash().getId())
                    .orElseThrow(() -> new NotFoundError(
                            NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                            Collections.singletonMap("CarwashId", "Carwash not found")
                    ));
            List<File> carwashImages = fileJPARepository.findByCarwash_IdAndIsDeletedFalse(carwash.getId());
            File carwashImage = carwashImages.stream().findFirst().orElse(null);

            recentReservations.add(new ReservationResponse.RecentReservation(reservation, carwashImage));
        }

        return new ReservationResponse.fetchRecentReservationDTO(recentReservations);
    }

    public ReservationResponse.PayAmountDTO findPayAmount(ReservationRequest.ReservationTimeDTO dto, Long bayId) {
        Bay bay = bayJPARepository.findById(bayId)
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("BayId", "Bay not found")
                ));

        Long carwashId = bay.getCarwash().getId();
        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("CarwashId", "Carwash not found")
                ));
        // 예약 시간 검증
        LocalDateTime startTime = dto.getStartTime();
        LocalDateTime endTime = dto.getEndTime();

        Optime optime = findOptime(carwash, startTime);
        validateReservationTime(startTime, endTime, optime, bayId);

        // 가격 계산
        int perPrice = carwash.getPrice();

        int minutesDifference = (int) ChronoUnit.MINUTES.between(startTime, endTime); //시간 차 계산
        int blocksOf30Minutes = minutesDifference / 30; //30분 단위로 계산
        int totalPrice = perPrice * blocksOf30Minutes;

        return new ReservationResponse.PayAmountDTO(startTime, endTime, totalPrice);
    }
}

