package bdbe.bdbd.service.member;

import bdbe.bdbd._core.exception.*;
import bdbe.bdbd._core.security.JWTProvider;
import bdbe.bdbd.dto.member.owner.OwnerResponse;
import bdbe.bdbd.model.bay.Bay;
import bdbe.bdbd.repository.bay.BayJPARepository;
import bdbe.bdbd.model.carwash.Carwash;
import bdbe.bdbd.repository.carwash.CarwashJPARepository;
import bdbe.bdbd.dto.member.user.UserRequest;
import bdbe.bdbd.dto.member.user.UserResponse;
import bdbe.bdbd.model.file.File;
import bdbe.bdbd.repository.file.FileJPARepository;
import bdbe.bdbd.model.member.Member;
import bdbe.bdbd.model.optime.Optime;
import bdbe.bdbd.repository.member.MemberJPARepository;
import bdbe.bdbd.repository.optime.OptimeJPARepository;
import bdbe.bdbd.model.reservation.Reservation;
import bdbe.bdbd.repository.reservation.ReservationJPARepository;
import bdbe.bdbd.dto.member.owner.OwnerResponse.OwnerDashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service

public class OwnerService {
    private final PasswordEncoder passwordEncoder;
    private final MemberJPARepository memberJPARepository;
    private final CarwashJPARepository carwashJPARepository;
    private final ReservationJPARepository reservationJPARepository;
    private final OptimeJPARepository optimeJPARepository;
    private final BayJPARepository bayJPARepository;
    private final FileJPARepository fileJPARepository;

    @Transactional
    public void join(UserRequest.JoinDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());

        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        try {
            memberJPARepository.save(requestDTO.toOwnerEntity(encodedPassword));
        } catch (Exception e) {
            throw new InternalServerError(
                    InternalServerError.ErrorCode.INTERNAL_SERVER_ERROR,
                    "unknown server error");
        }
    }


    public UserResponse.LoginResponse login(UserRequest.LoginDTO requestDTO) {
        Member memberPS = memberJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
                () -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("Email", "email not found : " + requestDTO.getEmail())
                ));

        if (!passwordEncoder.matches(requestDTO.getPassword(), memberPS.getPassword())) {
            throw new UnAuthorizedError(
                    UnAuthorizedError.ErrorCode.AUTHENTICATION_FAILED,
                    Collections.singletonMap("Password", "Wrong password")
            );
        }

        String userRole = String.valueOf(memberPS.getRole());
        if (!"ROLE_OWNER".equals(userRole) && !"ROLE_ADMIN".equals(userRole)) {
            throw new UnAuthorizedError(
                    UnAuthorizedError.ErrorCode.ACCESS_DENIED,
                    Collections.singletonMap("Role", "Cannot access page by your Role")
            );
        }

        String jwt = JWTProvider.create(memberPS);
        String redirectUrl = "/owner/home";

        return new UserResponse.LoginResponse(jwt, redirectUrl);
    }


    public void sameCheckEmail(String email) {
        Optional<Member> userOP = memberJPARepository.findByEmail(email);
        if (userOP.isPresent()) {
            throw new BadRequestError(
                    BadRequestError.ErrorCode.DUPLICATE_RESOURCE,
                    Collections.singletonMap("Email", "Duplicate email exist : " + email));
        }
    }

    public OwnerResponse.SaleResponseDTO findSales(List<Long> carwashIds, LocalDate selectedDate, Member sessionMember) {
        validateCarwashOwnership(carwashIds, sessionMember);

        List<Carwash> carwashList = carwashJPARepository.findCarwashesByMemberId(sessionMember.getId());

        List<Reservation> reservationList = reservationJPARepository.findAllByCarwash_IdInOrderByStartTimeDesc(carwashIds, selectedDate);
        if (reservationList.isEmpty()) return new OwnerResponse.SaleResponseDTO(carwashList, new ArrayList<>());

        return new OwnerResponse.SaleResponseDTO(carwashList, reservationList);
    }

    public OwnerResponse.SaleResponseDTO findCarwashList(Member sessionMember) {
        List<Carwash> carwashList = carwashJPARepository.findCarwashesByMemberId(sessionMember.getId());

        return new OwnerResponse.SaleResponseDTO(carwashList, null);
    }

    public OwnerResponse.ReservationCarwashListDTO findBayReservation(Long bayId, Member sessionMember) {
        validateBayOwnership(bayId, sessionMember);

        List<Reservation> reservationList = reservationJPARepository.findByBay_IdWithJoinsAndIsDeletedFalse(bayId);

        return new OwnerResponse.ReservationCarwashListDTO(reservationList);
    }

    /*
        owner가 해당 세차장의 주인인지 확인
     */
    private void validateCarwashOwnership(List<Long> carwashIds, Member sessionMember) {
        List<Long> userCarwashIds = carwashJPARepository.findCarwashIdsByMemberId(sessionMember.getId());

        if (!userCarwashIds.containsAll(carwashIds)) {
            throw new ForbiddenError(
                    ForbiddenError.ErrorCode.RESOURCE_ACCESS_FORBIDDEN,
                    Collections.singletonMap("MemberId", "Member is not the owner of the carwash.")
            );
        }
    }

    private void validateBayOwnership(Long bayId, Member sessionMember) {
        Bay bay = bayJPARepository.findById(bayId)
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("BayId", "Bay with id " + bayId + " not found")
                        ));

        Long carwashId = bay.getCarwash().getId();
        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("CarwashId", "Carwash not found"+ carwashId )
                ));
        if (carwash.getMember().getId() != sessionMember.getId()) {
            throw new ForbiddenError(
                    ForbiddenError.ErrorCode.RESOURCE_ACCESS_FORBIDDEN,
                    Collections.singletonMap("MemberId", "Member is not the owner of the carwash.")
            );
        }
    }


    public Map<String, Long> findMonthRevenue(List<Long> carwashIds, LocalDate selectedDate, Member sessionMember) {
        // 해당 유저가 운영하는 세차장의 id인지 확인
        List<Carwash> carwashList = carwashJPARepository.findAllByIdInAndMember_Id(carwashIds, sessionMember.getId());
        if (carwashIds.size() != carwashList.size())
            throw new ForbiddenError(
                    ForbiddenError.ErrorCode.RESOURCE_ACCESS_FORBIDDEN,
                    Collections.singletonMap("MemberId", "Member is not the owner of the carwash.")
            );

        // 매출 구하기 - 예약 삭제된 것 제외
        Map<String, Long> response = new HashMap<>();
        Long revenue = reservationJPARepository.findTotalRevenueByCarwashIdsAndDate(carwashIds, selectedDate);

        response.put("revenue", revenue);

        return response;
    }

    public OwnerResponse.ReservationOverviewResponseDTO fetchOwnerReservationOverview(Member sessionMember) {
        List<Carwash> carwashList = carwashJPARepository.findByMember_Id(sessionMember.getId());
        OwnerResponse.ReservationOverviewResponseDTO response = new OwnerResponse.ReservationOverviewResponseDTO();
        for (Carwash carwash : carwashList) {
            List<Bay> bayList = bayJPARepository.findByCarwashId(carwash.getId());
            List<Optime> optimeList = optimeJPARepository.findByCarwash_Id(carwash.getId());

            Date today = java.sql.Date.valueOf(LocalDate.now());
            List<Reservation> reservationList = reservationJPARepository.findTodaysReservationsByCarwashId(carwash.getId(), today);

            List<File> carwashImages = fileJPARepository.findByCarwash_IdAndIsDeletedFalse(carwash.getId());
            OwnerResponse.CarwashManageByOwnerDTO dto = new OwnerResponse.CarwashManageByOwnerDTO(carwash, bayList, optimeList, reservationList, carwashImages);
            response.addCarwashManageByOwnerDTO(dto);
        }

        return response;
    }

    public OwnerResponse.CarwashManageDTO fetchCarwashReservationOverview(Long carwashId, Member sessionMember) {
        // 세차장의 주인이 맞는지 확인하며 조회
        Carwash carwash = carwashJPARepository.findByIdAndMember_Id(carwashId, sessionMember.getId())
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("CarwashId", "Carwash not found"+ carwashId )
                ));

        LocalDate firstDayOfCurrentMonth = LocalDate.now().withDayOfMonth(1);

        Long monthlySales = reservationJPARepository.findTotalRevenueByCarwashIdAndDate(carwashId, firstDayOfCurrentMonth);
        Long monthlyReservations = reservationJPARepository.findMonthlyReservationCountByCarwashIdAndDate(carwashId, firstDayOfCurrentMonth);

        List<Bay> bayList = bayJPARepository.findByCarwashId(carwash.getId());
        List<Optime> optimeList = optimeJPARepository.findByCarwash_Id(carwash.getId());

        Date today = java.sql.Date.valueOf(LocalDate.now());
        List<Reservation> reservationList = reservationJPARepository.findTodaysReservationsByCarwashId(carwash.getId(), today);

        List<File> carwashImages = fileJPARepository.findByCarwash_IdAndIsDeletedFalse(carwash.getId());
        File carwashImage = carwashImages.stream().findFirst().orElse(null);
        OwnerResponse.CarwashManageDTO dto = new OwnerResponse.CarwashManageDTO(carwash, monthlySales, monthlyReservations, bayList, optimeList, reservationList, carwashImage);

        return dto;
    }

    public double calculateGrowthPercentage(Long currentValue, Long previousValue) {
        if (previousValue == 0 && currentValue == 0) {
            return 0;  // 이전 값과 현재 값이 모두 0인 경우 성장률은 0%로 간주
        } else if (previousValue == 0) {
            return 100;  // 이전 값이 0이고 현재 값이 0이 아닌 경우 성장률은 100%로 간주
        }
        return ((double) (currentValue - previousValue) / previousValue) * 100;
    }

    public OwnerDashboardDTO fetchOwnerHomepage(Member sessionMember) {
        List<Long> carwashIds = carwashJPARepository.findCarwashIdsByMemberId(sessionMember.getId());
        LocalDate firstDayOfCurrentMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate firstDayOfPreviousMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);

        Long currentMonthSales = reservationJPARepository.findTotalRevenueByCarwashIdsAndDate(carwashIds, firstDayOfCurrentMonth);
        Long previousMonthSales = reservationJPARepository.findTotalRevenueByCarwashIdsAndDate(carwashIds, firstDayOfPreviousMonth);
        Long currentMonthReservations = reservationJPARepository.findMonthlyReservationCountByCarwashIdsAndDate(carwashIds, firstDayOfCurrentMonth);
        Long previousMonthReservations = reservationJPARepository.findMonthlyReservationCountByCarwashIdsAndDate(carwashIds, firstDayOfPreviousMonth);

        double salesGrowthPercentage = calculateGrowthPercentage(currentMonthSales, previousMonthSales); // 전월대비 판매 성장률 (단위: %)
        double reservationGrowthPercentage = calculateGrowthPercentage(currentMonthReservations, previousMonthReservations); // 전월대비 예약 성장률 (단위: %)

        List<OwnerResponse.CarwashInfoDTO> carwashInfoDTOList = new ArrayList<>();
        for (Long carwashId : carwashIds) {
            Carwash carwash = carwashJPARepository.findById(carwashId)
                    .orElseThrow(() -> new NotFoundError(
                            NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                            Collections.singletonMap("CarwashId", "Carwash not found"+ carwashId )
                    ));
            Long monthlySales = reservationJPARepository.findTotalRevenueByCarwashIdAndDate(carwashId, firstDayOfCurrentMonth);

            Long monthlyReservations = reservationJPARepository.findMonthlyReservationCountByCarwashIdAndDate(carwashId, firstDayOfCurrentMonth);
            List<File> carwashImages = fileJPARepository.findByCarwash_IdAndIsDeletedFalse(carwashId);

            OwnerResponse.CarwashInfoDTO dto = new OwnerResponse.CarwashInfoDTO(carwash, monthlySales, monthlyReservations, carwashImages);
            carwashInfoDTOList.add(dto);
        }
        return new OwnerDashboardDTO(currentMonthSales, salesGrowthPercentage, currentMonthReservations, reservationGrowthPercentage, carwashInfoDTOList);
    }
}
