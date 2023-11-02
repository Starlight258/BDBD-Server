package bdbe.bdbd.member;

import bdbe.bdbd._core.errors.exception.BadRequestError;
import bdbe.bdbd._core.errors.exception.InternalServerError;
import bdbe.bdbd._core.errors.exception.UnAuthorizedError;
import bdbe.bdbd._core.errors.security.JWTProvider;
import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.bay.BayJPARepository;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.file.File;
import bdbe.bdbd.file.FileJPARepository;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.optime.OptimeJPARepository;
import bdbe.bdbd.reservation.Reservation;
import bdbe.bdbd.reservation.ReservationJPARepository;
import bdbe.bdbd.member.OwnerResponse.OwnerDashboardDTO;
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
    public void join(MemberRequest.JoinDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());

        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        try {
            memberJPARepository.save(requestDTO.toOwnerEntity(encodedPassword));
        } catch (Exception e) {
            throw new InternalServerError("unknown server error");
        }
    }


    public MemberResponse.LoginResponse login(MemberRequest.LoginDTO requestDTO) {
        Member memberPS = memberJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
                () -> new BadRequestError("email not found : " + requestDTO.getEmail())
        );

        if (!passwordEncoder.matches(requestDTO.getPassword(), memberPS.getPassword())) {
            throw new BadRequestError("wrong password");
        }

        // 여기서 사용자의 권한을 확인합니다.
        String userRole = String.valueOf(memberPS.getRole());
        if (!"ROLE_OWNER".equals(userRole) && !"ROLE_ADMIN".equals(userRole)) {
            throw new UnAuthorizedError("can't access this page");
        }

        String jwt = JWTProvider.create(memberPS);
        String redirectUrl = "/owner/home";

        return new MemberResponse.LoginResponse(jwt, redirectUrl);
    }


    public void sameCheckEmail(String email) {
        Optional<Member> userOP = memberJPARepository.findByEmail(email);
        if (userOP.isPresent()) {
            throw new BadRequestError("duplicate email exist : " + email);
        }
    }

    public OwnerResponse.SaleResponseDTO findSales(List<Long> carwashIds, LocalDate selectedDate, Member sessionMember) {
        validateCarwashOwnership(carwashIds, sessionMember);

        List<Carwash> carwashList = carwashJPARepository.findCarwashesByMemberId(sessionMember.getId()); // 유저가 가진 모든 세차장 (dto에서 사용)

        List<Reservation> reservationList = reservationJPARepository.findAllByCarwash_IdInOrderByStartTimeDesc(carwashIds, selectedDate);
        if (reservationList.isEmpty()) return new OwnerResponse.SaleResponseDTO(carwashList, new ArrayList<>());

        return new OwnerResponse.SaleResponseDTO(carwashList, reservationList);
    }

    /*
     owner가 해당 세차장의 주인인지 확인
     */
    private void validateCarwashOwnership(List<Long> carwashIds, Member sessionMember) {
        List<Long> userCarwashIds = carwashJPARepository.findCarwashIdsByMemberId(sessionMember.getId());

        if (!userCarwashIds.containsAll(carwashIds)) {
            throw new IllegalArgumentException("User is not the owner of the carwash. ");
        }
    }

    public Map<String, Long> findMonthRevenue(List<Long> carwashIds, LocalDate selectedDate, Member sessionMember) {
        // 해당 유저가 운영하는 세차장의 id인지 확인
        List<Carwash> carwashList = carwashJPARepository.findAllByIdInAndMember_Id(carwashIds, sessionMember.getId());
        if (carwashIds.size() != carwashList.size())
            throw new IllegalArgumentException("User is not the owner of the carwash.");
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

            List<File> carwashImages = fileJPARepository.findByCarwash_Id(carwash.getId());
            OwnerResponse.CarwashManageDTO dto = new OwnerResponse.CarwashManageDTO(carwash, bayList, optimeList, reservationList, carwashImages);
            response.addCarwashManageDTO(dto);
        }

        return response;
    }

    public OwnerResponse.CarwashManageDTO fetchCarwashReservationOverview(Long carwashId, Member sessionMember) {
        // 세차장의 주인이 맞는지 확인하며 조회
        Carwash carwash = carwashJPARepository.findByIdAndMember_Id(carwashId, sessionMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("carwash id :" + carwashId + " not found"));

        List<Bay> bayList = bayJPARepository.findByCarwashId(carwash.getId());
        List<Optime> optimeList = optimeJPARepository.findByCarwash_Id(carwash.getId());

        Date today = java.sql.Date.valueOf(LocalDate.now());
        List<Reservation> reservationList = reservationJPARepository.findTodaysReservationsByCarwashId(carwash.getId(), today);

        List<File> carwashImages = fileJPARepository.findByCarwash_Id(carwash.getId());
        OwnerResponse.CarwashManageDTO dto = new OwnerResponse.CarwashManageDTO(carwash, bayList, optimeList, reservationList, carwashImages);

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
                    .orElseThrow(() -> new EntityNotFoundException("Carwash not found with id: " + carwashId));
            // 판매 수익
            Long monthlySales = reservationJPARepository.findTotalRevenueByCarwashIdAndDate(carwashId, firstDayOfCurrentMonth);
            // 예약 수
            Long monthlyReservations = reservationJPARepository.findMonthlyReservationCountByCarwashIdAndDate(carwashId, firstDayOfCurrentMonth);
            List<File> carwashImages = fileJPARepository.findByCarwash_Id(carwashId);
            OwnerResponse.CarwashInfoDTO dto = new OwnerResponse.CarwashInfoDTO(carwash, monthlySales, monthlyReservations, carwashImages);
            carwashInfoDTOList.add(dto);
        }
        return new OwnerDashboardDTO(currentMonthSales, salesGrowthPercentage, currentMonthReservations, reservationGrowthPercentage, carwashInfoDTOList);
    }
}
