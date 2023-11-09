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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록 기능
    @PostMapping("/reviews")
    public ResponseEntity<?> createReview (@RequestBody @Valid ReviewRequest.SaveDTO saveDTO, Errors errors, @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.createReview(saveDTO, userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

}
