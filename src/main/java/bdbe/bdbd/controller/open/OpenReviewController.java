package bdbe.bdbd.controller.open;

import bdbe.bdbd._core.utils.ApiUtils;
import bdbe.bdbd.dto.review.ReviewResponse;
import bdbe.bdbd.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/open")
public class OpenReviewController {

    private final ReviewService reviewService;

    // 리뷰 조회 기능
    @GetMapping("/carwashes/{carwashId}/reviews")
    public ResponseEntity<?> getReviewsByCarwashId(@PathVariable("carwashId") Long carwashId) {
        ReviewResponse.ReviewResponseDTO dto = reviewService.getReviewsByCarwashId(carwashId);

        return ResponseEntity.ok(ApiUtils.success(dto));
    }

    // 리뷰 키워드 불러오기
    @GetMapping("/reviews")
    public ResponseEntity<?> getReviewKeyword() {
        ReviewResponse.ReviewKeywordResponseDTO dto = reviewService.getReviewKeyword();

        return ResponseEntity.ok(ApiUtils.success(dto));
    }

}
