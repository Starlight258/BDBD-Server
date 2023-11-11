package bdbe.bdbd.controller.user;

import bdbe.bdbd._core.security.CustomUserDetails;
import bdbe.bdbd._core.utils.ApiUtils;
import bdbe.bdbd.dto.review.ReviewRequest;
import bdbe.bdbd.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
/**
 * 사용자 리뷰 관련 요청을 처리하는 사용자 API
 *  - 리뷰 작성 기능
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(@RequestBody @Valid ReviewRequest.SaveDTO saveDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.createReview(saveDTO, userDetails.getMember());

        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
