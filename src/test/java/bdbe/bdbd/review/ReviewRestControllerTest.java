package bdbe.bdbd.review;

import bdbe.bdbd._core.exception.NotFoundError;
import bdbe.bdbd.dto.review.ReviewRequest;
import bdbe.bdbd.model.Code;
import bdbe.bdbd.model.Code.KeywordType;
import bdbe.bdbd.repository.bay.BayJPARepository;
import bdbe.bdbd.model.carwash.Carwash;
import bdbe.bdbd.repository.carwash.CarwashJPARepository;
import bdbe.bdbd.model.keyword.Keyword;
import bdbe.bdbd.repository.keyword.KeywordJPARepository;
import bdbe.bdbd.repository.location.LocationJPARepository;
import bdbe.bdbd.repository.review.ReviewJPARepository;
import bdbe.bdbd.model.reservation.Reservation;
import bdbe.bdbd.repository.reservation.ReservationJPARepository;
import bdbe.bdbd.repository.keyword.reviewKeyword.ReviewKeywordJPARepository;
import bdbe.bdbd.model.member.Member;
import bdbe.bdbd.repository.member.MemberJPARepository;
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
import java.util.Collections;
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
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("User", "User not found")
                ));

        PageRequest pageRequest = PageRequest.of(0, 1);
        List<Reservation> reservations = reservationJPARepository.findFirstByMemberIdWithJoinFetch(member.getId(), pageRequest);
        Reservation reservation = reservations.isEmpty() ? null : reservations.get(0);

        Carwash carwash = reservation.getBay().getCarwash();
        carwashId = carwash.getId();

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
                post("/api/user/reviews")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));

    }

    @WithUserDetails("user@nate.com")
    @Test
    @DisplayName("리뷰 조회 기능")
    public void find_review_test() throws Exception {
        // given
        this.createReviewTest();

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get(String.format("/api/open/carwashes/%d/reviews", carwashId))
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

    }

    @WithUserDetails("user@nate.com")
    @Test
    @DisplayName("리뷰 키워드 조회 기능")
    public void find_reviewKeyword_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get("/api/reviews")
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

    }
}
