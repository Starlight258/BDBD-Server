package bdbe.bdbd.reservation;

import bdbe.bdbd._core.exception.NotFoundError;
import bdbe.bdbd.model.bay.Bay;
import bdbe.bdbd.repository.bay.BayJPARepository;
import bdbe.bdbd.model.carwash.Carwash;
import bdbe.bdbd.repository.carwash.CarwashJPARepository;
import bdbe.bdbd.repository.keyword.KeywordJPARepository;
import bdbe.bdbd.repository.location.LocationJPARepository;
import bdbe.bdbd.model.reservation.Reservation;
import bdbe.bdbd.repository.optime.OptimeJPARepository;
import bdbe.bdbd.repository.reservation.ReservationJPARepository;
import bdbe.bdbd.dto.reservation.ReservationRequest.UpdateDTO;
import bdbe.bdbd.model.member.Member;
import bdbe.bdbd.repository.member.MemberJPARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ReservationRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ReservationJPARepository reservationJPARepository;

    @Autowired
    MemberJPARepository memberJPARepository;

    @Autowired
    CarwashJPARepository carwashJPARepository;

    @Autowired
    KeywordJPARepository keywordJPARepository;

    @Autowired
    BayJPARepository bayJPARepository;

    @Autowired
    LocationJPARepository locationJPARepository;

    @Autowired
    OptimeJPARepository optimeJPARepository;

    @Autowired
    private ObjectMapper om;

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("예약 수정 기능")
    public void updateReservation_test() throws Exception {
        //given
        Reservation reservation = reservationJPARepository.findAll()
                .stream()
                .filter(r -> !r.isDeleted())
                .findFirst().get();

        UpdateDTO updateDTO = new UpdateDTO();
        LocalDate date = LocalDate.now();
        updateDTO.setStartTime(LocalDateTime.of(date, LocalTime.of(14, 30)));
        updateDTO.setEndTime(LocalDateTime.of(date, LocalTime.of(15, 0)));

        String requestBody = om.writeValueAsString(updateDTO);
        System.out.println("요청 데이터 : " + requestBody);

        //when
        Long reservationId = reservation.getId();
        ResultActions resultActions = mvc.perform(
                put(String.format("/api/user/reservations/%d", reservationId))
                        .content(om.writeValueAsString(updateDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);
        reservation = reservationJPARepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("ReservationId", "Reservation not found"+reservationId)
                ));
        resultActions.andExpect(jsonPath("$.success").value("true"));


    }
    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("예약 취소 기능")
    public void deleteReservation_test() throws Exception {
        //given
        Reservation reservation = reservationJPARepository.findAll()
                .stream()
                .filter(r -> !r.isDeleted())
                .findFirst().get();
        System.out.println(reservation.getId());
        System.out.println(reservation.getBay().getId());
        System.out.println(reservation.getMember().getId());


        //when
        Long reservationId = reservation.getId();
        System.out.println("reservation id: " +reservationId);
        ResultActions resultActions = mvc.perform(
                delete(String.format("/api/user/reservations/%d", reservationId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);
        resultActions.andExpect(jsonPath("$.success").value("true"));


    }


    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("세차장별 예약 조회 내역 기능")
    public void findAllByCarwash_test() throws Exception {
        //given
        Carwash carwash = carwashJPARepository.findFirstBy();
        System.out.println("carwashId : " + carwash.getId());
        Bay bay = bayJPARepository.findByCarwashId(carwash.getId()).get(0);
        System.out.println("bayId : " + bay.getId());

        Member member = memberJPARepository.findByEmail("user@nate.com")
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("UserId", "UserId not found")
                ));
        // 예약 1
        Reservation reservation = Reservation.builder()
                .price(5000)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(30))
                .bay(bay)
                .member(member)
                .build();
        reservationJPARepository.save(reservation);
        // 예약 2
        LocalDate date = LocalDate.now();
        reservation = Reservation.builder()
                .price(4000)
                .startTime(LocalDateTime.of(date, LocalTime.of(20, 0)))
                .endTime(LocalDateTime.of(date, LocalTime.of(20, 30)))
                .bay(bay)
                .member(member)
                .build();
        reservationJPARepository.save(reservation);

        //when
        ResultActions resultActions = mvc.perform(
                get(String.format("/api/open/carwashes/%d/bays", carwash.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);
        resultActions.andExpect(jsonPath("$.success").value("true"));

    }

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("결제 후 예약 내역 조회")
    public void fetchLatestReservation_test() throws Exception {
        //given
        Member member = memberJPARepository.findByEmail("user@nate.com")
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("UserId", "UserId not found")
                ));
        Bay savedBay = bayJPARepository.findFirstBy();

        // 예약 1
        Reservation reservation = Reservation.builder()
                .price(4000)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(30))
                .bay(savedBay)
                .member(member)
                .build();
        reservationJPARepository.save(reservation);

        //when
        ResultActions resultActions = mvc.perform(
                get("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

    }

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("현재 시간 기준 예약 내역 조회")
    public void fetchCurrentStatusReservation_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(
                get("/api/reservations/current-status")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

    }

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("최근 예약 내역 조회")
    public void fetchRecentReservation_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(
                get("/api/reservations/recent")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

    }

}
