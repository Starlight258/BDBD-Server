package bdbe.bdbd.review;

import bdbe.bdbd.bay.BayJPARepository;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.keyword.Keyword;
import bdbe.bdbd.keyword.KeywordJPARepository;
import bdbe.bdbd.keyword.KeywordType;
import bdbe.bdbd.location.LocationJPARepository;
import bdbe.bdbd.reservation.Reservation;
import bdbe.bdbd.reservation.ReservationJPARepository;
import bdbe.bdbd.keyword.reviewKeyword.ReviewKeywordJPARepository;
import bdbe.bdbd.member.Member;
import bdbe.bdbd.member.MemberJPARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ReviewRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ReviewJPARepository reviewJPARepository;

    @Autowired
    LocationJPARepository locationJPARepository;

    @Autowired
    MemberJPARepository memberJPARepository;

    @Autowired
    CarwashJPARepository carwashJPARepository;

    @Autowired
    KeywordJPARepository keywordJPARepository;

    @Autowired
    ReviewKeywordJPARepository reviewKeywordJPARepository;

    @Autowired
    BayJPARepository bayJPARepository;

    @Autowired
    ReservationJPARepository reservationJPARepository;

    @Autowired
    private ObjectMapper om;

    Long carwashId;

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("리뷰 등록 기능")
    public void createReviewTest() throws Exception {
        // given
        Member member = memberJPARepository.findByEmail("user@nate.com")
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        // 예약 가져오기
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<Reservation> reservations = reservationJPARepository.findFirstByMemberIdWithJoinFetch(member.getId(), pageRequest);
        Reservation reservation = reservations.isEmpty() ? null : reservations.get(0);

        Carwash carwash = reservation.getBay().getCarwash();
        carwashId = carwash.getId();

        // 키워드
        List<Keyword> keywordList = keywordJPARepository.findByType(KeywordType.REVIEW);

        List<Long> keywordIds = keywordList.stream()
                .map(Keyword::getId)
                .collect(Collectors.toList());
        System.out.println("idList:");
        for (Long keywordId : keywordIds) {
            System.out.println("keywordId = " + keywordId);
        }

        ReviewRequest.SaveDTO dto = new ReviewRequest.SaveDTO();
        dto.setCarwashId(carwash.getId());
        dto.setKeywordIdList(keywordIds);
        dto.setReservationId(reservation.getId());
        dto.setRate(5);
        dto.setComment("좋네요");


        String requestBody = om.writeValueAsString(dto);
        System.out.println("요청 데이터 : " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/reviews")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));

    }

//    @WithUserDetails(value = "user@nate.com")
//    @Test
//    @DisplayName("리뷰 별점 확인 코드")
//    public void checkRateTest() throws Exception {
//        // given
//        Location location = Location.builder().address("address").latitude(10).longitude(20).place("예쁨").build();
//        Location savedLocation = locationJPARepository.save(location);
//
//        User user = User.builder()
//                .role(UserRole.ROLE_USER)
//                .email("hi5@nate.com")
//                .password("user1234!")
//                .username("useruser")
//                .build();
//        User savedUser = userJPARepository.save(user);
//
//        Carwash carwash = Carwash.builder()
//                .name("세차장")
//                .des("좋은 세차장입니다.")
//                .tel("010-2222-3333")
//                .location(savedLocation)
//                .user(savedUser)
//                .build();
//        Carwash savedCarwash = carwashJPARepository.save(carwash);
//        // 키워드
//        List<Keyword> keywordList = new ArrayList<>();
//        Keyword keyword = Keyword.builder().name("하부세차").build();
//        keywordList.add(keyword);
//        Keyword keyword2 = Keyword.builder().name("야간 조명").build();
//        keywordList.add(keyword2);
//        List<Keyword> savedKeywordList = keywordJPARepository.saveAll(keywordList);
//        List<Long> keywordIds = savedKeywordList.stream()
//                .map(Keyword::getId)
//                .collect(Collectors.toList());
//
//        Bay bay = Bay.builder()
//                .bayNum(10)
//                .carwash(savedCarwash)
//                .status(1)
//                .build();
//        Bay savedBay = bayJPARepository.save(bay);
//
//        LocalDate date = LocalDate.now();
//        Reservation reservation = Reservation.builder()
//                .id(20L)
//                .price(5000)
//                .startTime(LocalDateTime.of(date, LocalTime.of(6, 0))) // 오전 6시
//                .endTime(LocalDateTime.of(date, LocalTime.of(6, 30))) // 30분 뒤
//                .bay(savedBay)
//                .user(user)
//                .build();
//        Reservation savedReservation = reservationJPARepository.save(reservation);
//
//        //dto 보냄
//        ReviewRequest.SaveDTO dto = new ReviewRequest.SaveDTO();
//        dto.setCarwashId(13L);
//        dto.setKeywordList(keywordIds);
//        dto.setReservationId(savedReservation.getId());
//        dto.setRate(1);
//        dto.setComment("좋네요");
//
//        String requestBody = om.writeValueAsString(dto);
//        System.out.println("요청 데이터 : " + requestBody);
//
//        // when
//        ResultActions resultActions = mvc.perform(
//                post("/reviews")
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//        );
//
//        // eye
//        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
//        System.out.println("응답 Body : " + responseBody);
//
//        // verify
//
//        Optional<Carwash> byId1 = carwashJPARepository.findById(13L);
//        if (byId1.isPresent()) {
//            Carwash carwash1 = byId1.get();
////            assertThat(carwash1.getRate()).isEqualTo(4);
//        }
//    }
    @WithUserDetails("user@nate.com")
    @Test
    @DisplayName("리뷰 조회 기능")
    public void find_review_test() throws Exception {
        // given
        this.createReviewTest(); // 이것으로 인해 userDetails가 필요하다. (테스트코드에서만 필요)
        // NOTE: carwashId 확인하기
        System.out.println("carwash id : " + carwashId);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get(String.format("/api/carwashes/%d/reviews", carwashId))
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
    }

    @WithUserDetails("user@nate.com")
    @Test
    @DisplayName("리뷰 키워드 조회 기능")
    public void find_reviewKeyword_test() throws Exception {
        // given
        // 키워드 만들기
//        List<Keyword> keywordList = new ArrayList<>();
//        Keyword keyword = Keyword.builder().name("에어컨").type(1).build();
//        keywordList.add(keyword);
//        Keyword keyword2 = Keyword.builder().name("하부 세차").type(1).build();
//        keywordList.add(keyword2);
//
//        Keyword keyword3 = Keyword.builder().name("사장님이 친절해요").type(2).build();
//        keywordList.add(keyword3);
//        Keyword keyword4 = Keyword.builder().name("베이마다 에어컨이 있어요").type(2).build();
//        keywordList.add(keyword4);
//        keywordJPARepository.saveAll(keywordList);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get("/api/reviews")
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
    }
}
