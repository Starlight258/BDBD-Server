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
/**
 * 리뷰 조회 기능을 제공하는 공개 API
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/open")
public class OpenReviewController {

    private final ReviewService reviewService;

    @GetMapping("/carwashes/{carwash-id}/reviews")
    public ResponseEntity<?> getReviewsByCarwashId(@PathVariable("carwash-id") Long carwashId) {
        ReviewResponse.ReviewResponseDTO dto = reviewService.getReviewsByCarwashId(carwashId);

        return ResponseEntity.ok(ApiUtils.success(dto));
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> getReviewKeyword() {
        ReviewResponse.ReviewKeywordResponseDTO dto = reviewService.getReviewKeyword();

        return ResponseEntity.ok(ApiUtils.success(dto));
    }
}
