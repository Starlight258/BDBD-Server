package bdbe.bdbd.controller.member.user;


import bdbe.bdbd._core.exception.BadRequestError;
import bdbe.bdbd._core.security.CustomUserDetails;
import bdbe.bdbd._core.security.JWTProvider;
import bdbe.bdbd._core.utils.ApiUtils;
import bdbe.bdbd.dto.member.user.UserRequest;
import bdbe.bdbd.dto.member.user.UserResponse;
import bdbe.bdbd.service.member.UserService;
import bdbe.bdbd.dto.member.owner.OwnerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;

    @PostMapping("/public/member/check")
    public ResponseEntity<?> check(@RequestBody @Valid UserRequest.EmailCheckDTO emailCheckDTO, Errors errors) {
        userService.sameCheckEmail(emailCheckDTO.getEmail());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/public/join/user")
    public ResponseEntity<?> joinUser(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Errors errors) {
        userService.join(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/public/login/user")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDTO requestDTO, Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
            throw new BadRequestError(errorMessage);
        }
        UserResponse.LoginResponse response = userService.login(requestDTO);
        return ResponseEntity.ok().header(JWTProvider.HEADER, response.getJwtToken()).body(ApiUtils.success(null));
    }

    @GetMapping("/common/member/info")
    public ResponseEntity<?> findUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null || userDetails.getMember() == null) {
            // 인증 정보가 없는 경우 적절한 상태 코드와 메시지를 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiUtils.error("Authentication is required to access this resource.", HttpStatus.UNAUTHORIZED));
        }
        OwnerResponse.UserInfoDTO dto = userService.findUserInfo(userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(dto));
    }

    // 로그아웃 사용안함 - 프론트에서 JWT 토큰을 브라우저의 localstorage에서 삭제하면 됨.
}

