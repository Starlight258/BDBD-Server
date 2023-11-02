package bdbe.bdbd.reservation;


import bdbe.bdbd._core.errors.utils.DateUtils;
import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.bay.BayJPARepository;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.file.File;
import bdbe.bdbd.file.FileJPARepository;
import bdbe.bdbd.keyword.reviewKeyword.ReviewKeywordJPARepository;
import bdbe.bdbd.location.Location;
import bdbe.bdbd.location.LocationJPARepository;
import bdbe.bdbd.optime.DayType;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.optime.OptimeJPARepository;
import bdbe.bdbd.reservation.ReservationResponse.ReservationInfoDTO;
import bdbe.bdbd.review.ReviewJPARepository;
import bdbe.bdbd.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    private final ReviewJPARepository reviewJPARepository;
    private final ReviewKeywordJPARepository reviewKeywordJPARepository;

    @Transactional
    public void save(ReservationRequest.SaveDTO dto, Long carwashId, Long bayId, Member sessionMember) {
        Carwash carwash = findCarwashById(carwashId);
        Optime optime = findOptime(carwash, dto.getStartTime());

        validateReservationTime(dto, optime, bayId);
        Bay bay = findBayById(bayId);

        Reservation reservation = dto.toReservationEntity(carwash, bay, sessionMember);
        reservationJPARepository.save(reservation);
    }

    @Transactional
    public void update(ReservationRequest.UpdateDTO dto, Long reservationId) {
        Reservation reservation = reservationJPARepository.findById(reservationId)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Reservation with id " + reservationId + " not found"));
        Long carwashId = reservation.getBay().getCarwash().getId();
        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new IllegalArgumentException("Carwash with id " + carwashId + " not found"));

        reservation.updateReservation(dto.getStartTime(), dto.getEndTime(), carwash);

    }

    @Transactional
    public void delete(Long reservationId) {
        Reservation reservation = reservationJPARepository.findById(reservationId)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Reservation with id " + reservationId + " not found"));
        reservation.changeDeletedFlag(true);
    }


    private Carwash findCarwashById(Long id) {
        return carwashJPARepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("carwash not found"));
    }

    private Optime findOptime(Carwash carwash, LocalDateTime startTime) {
        List<Optime> optimeList = optimeJPARepository.findByCarwash_Id(carwash.getId());
        DayOfWeek dayOfWeek = startTime.getDayOfWeek();
        DayType dayType = DateUtils.getDayType(dayOfWeek);
        return optimeList.stream()
                .filter(o -> o.getDayType() == dayType)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("carwash optime doesn't exist"));
    }

    private void validateReservationTime(ReservationRequest.SaveDTO dto, Optime optime, Long bayId) {
        LocalTime opStartTime = optime.getStartTime();
        LocalTime opEndTime = optime.getEndTime();
        LocalTime dtoStartTimePart = dto.getStartTime().toLocalTime();
        LocalTime dtoEndTimePart = dto.getEndTime().toLocalTime();

        // 예약이 운영시간을 넘지 않도록 함
        if (!((opStartTime.isBefore(dtoStartTimePart) || opStartTime.equals(dtoStartTimePart)) &&
                (opEndTime.isAfter(dtoEndTimePart) || opEndTime.equals(dtoEndTimePart)))) {
            throw new IllegalArgumentException("Reservation time is out of operating hours");
        }
        // 이미 예약된 시간은 피하도록 함
        List<Reservation> reservationList = reservationJPARepository.findByBay_Id(bayId);

        boolean isOverlapping = reservationList.stream()
                .anyMatch(existingReservation -> {
                    LocalDateTime existingStartTime = existingReservation.getStartTime();
                    LocalDateTime existingEndTime = existingReservation.getEndTime();
                    return !(
                            (dto.getEndTime().isBefore(existingStartTime) || dto.getEndTime().isEqual(existingStartTime)) ||
                                    (dto.getStartTime().isAfter(existingEndTime) || dto.getStartTime().isEqual(existingEndTime))
                    );
                });

        if (isOverlapping) {
            throw new IllegalArgumentException("Reservation time overlaps with an existing reservation.");
        }

    }

    private Bay findBayById(Long id) {
        return bayJPARepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("bay not found"));
    }

    //
    public ReservationResponse.findAllResponseDTO findAllByCarwash(Long carwashId) {
        //베이에서 해당 세차장 id와 관련된 베이 객체 모두 찾기
        List<Bay> bayList = bayJPARepository.findByCarwashId(carwashId);
        // id만 추출하기
        List<Long> bayIdList = bayJPARepository.findIdsByCarwashId(carwashId);
        //예약에서 베이 id 리스트로 모두 찾기
        List<Reservation> reservationList = reservationJPARepository.findByBayIdIn(bayIdList)
                .stream()
                .filter(r -> !r.isDeleted())
                .collect(Collectors.toList());
        return new ReservationResponse.findAllResponseDTO(bayList, reservationList);
    }

    public ReservationResponse.findLatestOneResponseDTO fetchLatestReservation(Member sessionMember) {
        // 가장 최근의 예약 찾기
        Reservation reservation = reservationJPARepository.findTopByMemberIdOrderByIdDesc(sessionMember.getId())
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("no reservation found"));
        // 예약과 관련된 베이 찾기
        Bay bay = bayJPARepository.findById(reservation.getBay().getId())
                .orElseThrow(() -> new NoSuchElementException("no bay found"));
        // 베이가 속해있는 세차장 찾기
        Carwash carwash = carwashJPARepository.findById(bay.getCarwash().getId())
                .orElseThrow(() -> new NoSuchElementException("no carwash found"));
        // 세차장이 위치한 위치 찾기
        Location location = locationJPARepository.findById(carwash.getLocation().getId())
                .orElseThrow(() -> new NoSuchElementException("no location found"));
        List<File> carwashImages = fileJPARepository.findByCarwash_Id(carwash.getId());
        return new ReservationResponse.findLatestOneResponseDTO(reservation, bay, carwash, location, carwashImages);
    }

    public ReservationResponse.fetchCurrentStatusReservationDTO fetchCurrentStatusReservation(Member sessionMember) {
        // 유저의 예약내역 모두 조회
        List<Reservation> reservationList = reservationJPARepository.findByMemberId(sessionMember.getId())
                .stream()
                .filter(r -> !r.isDeleted())
                .collect(Collectors.toList());
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
                    .orElseThrow(() -> new EntityNotFoundException("Bay not found"));
            Carwash carwash = carwashJPARepository.findById(bay.getCarwash().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Carwash not found"));

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
                throw new IllegalStateException("reservation id: " + reservation.getId() + " not found");
            }
        }
        return new ReservationResponse.fetchCurrentStatusReservationDTO(current, upcoming, completed);
    }

    public ReservationResponse.fetchRecentReservationDTO fetchRecentReservation(Member sessionMember) {
        // 유저의 예약내역 모두 조회
        Pageable pageable = PageRequest.of(0, 5); // 최대 5개까지만 가져오기
        List<Reservation> reservationList = reservationJPARepository.findByMemberIdJoinFetch(sessionMember.getId(), pageable)
                .stream()
                .filter(r -> !r.isDeleted())
                .collect(Collectors.toList());

        return new ReservationResponse.fetchRecentReservationDTO(reservationList);
    }

}
