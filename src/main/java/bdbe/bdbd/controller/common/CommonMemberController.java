package bdbe.bdbd.controller.common;


import bdbe.bdbd._core.exception.UnAuthorizedError;
import bdbe.bdbd._core.security.CustomUserDetails;
import bdbe.bdbd._core.utils.ApiUtils;
import bdbe.bdbd.dto.member.owner.OwnerResponse;
import bdbe.bdbd.service.member.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
/**
 * 사용자와 사장님의 요청을 처리하는 공통 API
 */
@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class CommonMemberController {

    private final UserService userService;

    @GetMapping("/member/info")
    public ResponseEntity<?> findUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null || userDetails.getMember() == null) {
            throw new UnAuthorizedError(
                    UnAuthorizedError.ErrorCode.ACCESS_DENIED,
                    Collections.singletonMap("Token", "Authentication is required to access this resource.")
            );
        }
        OwnerResponse.UserInfoDTO userInfoDto = userService.findUserInfo(userDetails.getMember());

        return ResponseEntity.ok(ApiUtils.success(userInfoDto));
    }
}

