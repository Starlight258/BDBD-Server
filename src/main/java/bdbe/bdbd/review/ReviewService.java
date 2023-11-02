package bdbe.bdbd.review;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.keyword.Keyword;
import bdbe.bdbd.keyword.KeywordJPARepository;
import bdbe.bdbd.keyword.KeywordType;
import bdbe.bdbd.reservation.Reservation;
import bdbe.bdbd.reservation.ReservationJPARepository;
import bdbe.bdbd.review.ReviewResponse.ReviewByCarwashIdDTO;
import bdbe.bdbd.review.ReviewResponse.ReviewKeywordResponseDTO;
import bdbe.bdbd.review.ReviewResponse.ReviewResponseDTO;
import bdbe.bdbd.keyword.reviewKeyword.ReviewKeyword;
import bdbe.bdbd.keyword.reviewKeyword.ReviewKeywordJPARepository;
import bdbe.bdbd.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewJPARepository reviewJPARepository;
    private final CarwashJPARepository carwashJPARepository;
    private final ReservationJPARepository reservationJPARepository;
    private final ReviewKeywordJPARepository reviewKeywordJPARepository;
    private final KeywordJPARepository keywordJPARepository;

    @Transactional
    public void createReview(ReviewRequest.SaveDTO dto, Member member) {
        Carwash carwash = carwashJPARepository.findById(dto.getCarwashId())
                .orElseThrow(() -> new IllegalArgumentException("Carwash not found"));
        Reservation reservation = reservationJPARepository.findById(dto.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        Review review = dto.toReviewEntity(member, carwash, reservation);
        log.info("review: {}", review);

        Review savedReview = reviewJPARepository.save(review);
        // 리뷰 키워드 저장
        List<Long> keywordIdList = dto.getKeywordIdList();

        keywordIdList.stream()
                .map(id -> {
                    Keyword keyword = keywordJPARepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("keyword not found"));
                    ReviewKeyword reviewKeyword = ReviewKeyword.builder().keyword(keyword).review(savedReview).build();
                    ReviewKeyword savedReviewKeyword = reviewKeywordJPARepository.save(reviewKeyword);
                    System.out.println("reviewKeyword:" + savedReviewKeyword.toString());
                    return savedReviewKeyword;
                })
                .collect(Collectors.toList());

        updateAverageRate(dto, carwash);

    }

    private void updateAverageRate(ReviewRequest.SaveDTO dto, Carwash carwash) {
        // 세차장에 평점 저장
        // 고객이 보낸 평점
        double clientRate = dto.getRate();
        // 세차장 평점
        double carwashRate = carwash.getRate();
        // 세차장 리뷰 몇 개 있는지
        long num = reviewJPARepository.countByCarwash_Id(carwash.getId());
        //평균 구하기
        double totalScore = 0;
        totalScore += clientRate;
        totalScore += (carwashRate * (num - 1));
        double rate = totalScore / num;
        carwash.updateRate(rate);
    }

    public ReviewResponseDTO getReviewsByCarwashId(Long carwashId) {
        ReviewResponse.ReviewOverviewDTO overviewDTO = new ReviewResponse.ReviewOverviewDTO();
        List<Review> reviews = reviewJPARepository.findByCarwash_Id(carwashId);
        setOverviewDTO(overviewDTO, carwashId, reviews);

        Map<Long, Integer> keywordCountMap = countKeywordFrequency(reviews);

        setReviewKeywordCounts(overviewDTO, keywordCountMap);
        List<ReviewByCarwashIdDTO> carwashDTOs = createCarwashDTOs(reviews);

        return new ReviewResponseDTO(overviewDTO, carwashDTOs);
    }

    private void setOverviewDTO(ReviewResponse.ReviewOverviewDTO overviewDTO, Long carwashId, List<Review> reviews) {
        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new IllegalArgumentException("carwash not found"));
        overviewDTO.setRate(carwash.getRate());
        overviewDTO.setTotalCnt(reviews.size());
    }

    private Map<Long, Integer> countKeywordFrequency(List<Review> reviews) {
        Map<Long, Integer> keywordCountMap = new HashMap<>();
        for (Review review : reviews) {
            List<ReviewKeyword> reviewKeywords = reviewKeywordJPARepository.findByReview_Id(review.getId());
            for (ReviewKeyword reviewKeyword : reviewKeywords) {
                keywordCountMap.put(reviewKeyword.getKeyword().getId(),
                        keywordCountMap.getOrDefault(reviewKeyword.getKeyword().getId(), 0) + 1);
            }
        }
        return keywordCountMap;
    }

    private void setReviewKeywordCounts(ReviewResponse.ReviewOverviewDTO overviewDTO, Map<Long, Integer> keywordCountMap) {
        List<ReviewResponse.ReviewKeywordCnt> keywordCounts = keywordCountMap.entrySet().stream()
                .map(entry -> new ReviewResponse.ReviewKeywordCnt(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        overviewDTO.setReviewKeyword(keywordCounts);
    }

    private List<ReviewByCarwashIdDTO> createCarwashDTOs(List<Review> reviews) {
        return reviews.stream()
                .map(review -> {
                    List<ReviewKeyword> reviewKeywords = reviewKeywordJPARepository.findByReview_Id(review.getId());
                    return new ReviewByCarwashIdDTO(review, review.getMember(), reviewKeywords);
                })
                .collect(Collectors.toList());
    }



    public ReviewKeywordResponseDTO getReviewKeyword() {
        List<Keyword> keywordList = keywordJPARepository.findByType(KeywordType.REVIEW);

        return new ReviewKeywordResponseDTO(keywordList);
    }

}