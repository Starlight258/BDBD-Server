package bdbe.bdbd.reservation;

import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.bay.BayJPARepository;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.keyword.KeywordJPARepository;
import bdbe.bdbd.location.Location;
import bdbe.bdbd.location.LocationJPARepository;
import bdbe.bdbd.optime.DayType;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.optime.OptimeJPARepository;
import bdbe.bdbd.reservation.ReservationRequest.SaveDTO;
import bdbe.bdbd.user.User;
import bdbe.bdbd.user.UserJPARepository;
import bdbe.bdbd.user.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static bdbe.bdbd.optime.DayType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//통합테스트(SF-F-DS(Handler, ExHandler)-C-S-R-PC-DB) 다 뜬다.
public class ReservationRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ReservationJPARepository reservationJPARepository;

    @Autowired
    UserJPARepository userJPARepository;

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

    private User user;

    @BeforeEach()
    public void setup() {
//        Location location = Location.builder()
//                .place("place")
//                .address("address")
//                .latitude(1.234)
//                .longitude(2.345)
//                .build();
//        Location savedLocation = locationJPARepository.save(location);
//
//        User user = User.builder()
//                .role(UserRole.ROLE_USER)
//                .email("user@nate.com")
//                .password("user1234!")
//                .username("useruser")
//                .build();
//        User savedUser = userJPARepository.findFirstBy();
//
//
////        Carwash savedCarwash = carwashJPARepository.findFirstBy();
//        Carwash carwash = Carwash.builder()
//                .price(100)
//                .name("세차장")
//                .des("좋은 세차장입니다.")
//                .tel("010-2222-3333")
//                .location(savedLocation)
//                .user(savedUser)
//                .build();
//        Carwash savedCarwash = carwashJPARepository.save(carwash);
//
////        Long id, int bayNum, int bayType, Carwash carwash, int status
//        Bay bay = Bay.builder()
//                .bayNum(1)
//                .carwash(savedCarwash)
//                .status(1)
//                .build();
//        bayJPARepository.save(bay);

//        Carwash carwash = carwashJPARepository.findFirstBy();
////
//        Optime optime = Optime.builder()
//                .dayType(WEEKDAY)
//                .startTime(LocalTime.of(6, 0)) // 오전 6시
//                .endTime(LocalTime.of(22, 0)) // 오후 10시
//                .carwash(carwash)
//                .build();
//        Optime savedOptime = optimeJPARepository.save(optime);
//
//        optime = Optime.builder()
//                .dayType(WEEKEND)
//                .startTime(LocalTime.of(6, 0)) // 오전 6시
//                .endTime(LocalTime.of(23, 59)) // 오후 11시 59분
//                .carwash(carwash)
//                .build();
//        savedOptime = optimeJPARepository.save(optime);

    }


    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("예약 기능")
    public void save_test() throws Exception {
        // given
        Long bayId;
        Bay bay = bayJPARepository.findFirstBy();
        if(bay != null) {
            bayId = bay.getId();
            System.out.println("bayId : " + bayId);
        } else {
            System.out.println("No Bay entity found");
             throw new EntityNotFoundException("No Bay entity found");
        }
        Long carwashId = bay.getCarwash().getId();
//        // dto 생성
        SaveDTO saveDTO = new SaveDTO();
        // SaveDTO 객체 생성 및 값 설정
        saveDTO.setBayId(bayId);
        LocalDate date = LocalDate.now();
        saveDTO.setStartTime(LocalDateTime.of(date, LocalTime.of(6, 0))); // 오전 6시
        saveDTO.setEndTime(LocalDateTime.of(date, LocalTime.of(6, 30))); // 30분 뒤
        String s = saveDTO.toString();
        System.out.println(s);
        String requestBody = om.writeValueAsString(saveDTO);
        System.out.println("요청 데이터 : " + requestBody);
//         when
//        /carwashes/{carwash_id}/bays/{bay_id}/reservations
        ResultActions resultActions = mvc.perform(
                post(String.format("/carwashes/%d/bays/%d/reservations", carwashId, bayId))
                        .content(om.writeValueAsString(saveDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

//        // eye(1)
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
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

        User user = userJPARepository.findByEmail("user@nate.com").orElseThrow(()->new IllegalArgumentException("user not found"));
        // 예약 1
        Reservation reservation = Reservation.builder()
                .price(5000)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(30)) //30분 뒤로 설정
                .bay(bay)
                .user(user)
                .build();
        reservationJPARepository.save(reservation);
        // 예약 2
        Reservation reservation2 = Reservation.builder()
                .price(5000)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(60)) //60분 뒤로 설정
                .bay(bay)
                .user(user)
                .build();
        reservationJPARepository.save(reservation2);

        //when
        ResultActions resultActions = mvc.perform(
                get(String.format("/carwashes/%d/bays", carwash.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //then
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);


    }

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("결제 후 예약 내역 조회")
    public void fetchLatestReservation_test() throws Exception {
        //given
        User user = userJPARepository.findByEmail("user@nate.com")
                .orElseThrow(()-> new IllegalArgumentException("user not found"));
        Bay savedBay = bayJPARepository.findFirstBy();

        // 예약 1
        Reservation reservation = Reservation.builder()
                .price(4000)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(30)) //30분 뒤로 설정
                .bay(savedBay)
                .user(user)
                .build();
        reservationJPARepository.save(reservation);

        //when
        ResultActions resultActions = mvc.perform(
                get("/reservations")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //then
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

    }

//    /reservations/current-status
    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("현재 시간 기준 예약 내역 조회")
    public void fetchCurrentStatusReservation_test() throws Exception {
        //given

        User user = userJPARepository.findByEmail("user@nate.com")
                .orElseThrow(()-> new IllegalArgumentException("user not found"));
        Bay savedBay = bayJPARepository.findFirstBy();

        LocalDate date = LocalDate.now();
        // 예약 1
        Reservation reservation = Reservation.builder()
                .price(4000)
                .startTime(LocalDateTime.of(date, LocalTime.of(6, 0))) // 오전 6시
                .endTime(LocalDateTime.of(date, LocalTime.of(6, 30))) // 30분 뒤
                .bay(savedBay)
                .user(user)
                .build();
        reservationJPARepository.save(reservation);

        // 예약 2
        reservation = Reservation.builder()
                .price(4000)
                .startTime(LocalDateTime.of(date, LocalTime.of(20, 0))) // 오전 6시
                .endTime(LocalDateTime.of(date, LocalTime.of(20, 30))) // 30분 뒤
                .bay(savedBay)
                .user(user)
                .build();
        reservationJPARepository.save(reservation);

        // 예약 3
        reservation = Reservation.builder()
                .price(4000)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(30)) //30분 뒤로 설정
                .bay(savedBay)
                .user(user)
                .build();
        reservationJPARepository.save(reservation);



        //when
        ResultActions resultActions = mvc.perform(
                get("/reservations/current-status")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //then
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

    }



}
