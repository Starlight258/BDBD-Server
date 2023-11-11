package bdbe.bdbd.controller.open;


import bdbe.bdbd._core.security.JWTProvider;
import bdbe.bdbd._core.utils.ApiUtils;
import bdbe.bdbd._core.utils.MemberUtils;
import bdbe.bdbd.dto.member.user.UserRequest;
import bdbe.bdbd.dto.member.user.UserResponse;
import bdbe.bdbd.service.member.OwnerService;
import bdbe.bdbd.service.member.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * USER와 OWNER의 가입 및 로그인 기능을 포함하는 공개 API
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/open")
public class OpenMemberController {

    private final UserService userService;
    private final OwnerService ownerService;
    private final MemberUtils memberUtils;

    @PostMapping("/member/check")
    public ResponseEntity<?> checkMember(@RequestBody @Valid UserRequest.EmailCheckDTO emailCheckDTO, Errors errors) {
        memberUtils.checkSameEmail(emailCheckDTO.getEmail());

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/join/user")
    public ResponseEntity<?> joinUser(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Errors errors) {
        userService.join(requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/login/user")
    public ResponseEntity<?> loginUser(@RequestBody @Valid UserRequest.LoginDTO requestDTO, Errors errors) {
        UserResponse.LoginResponse response = userService.login(requestDTO);

        return ResponseEntity.ok().header(JWTProvider.HEADER, response.getJwtToken()).body(ApiUtils.success(null));
    }

    @PostMapping("/join/owner")
    public ResponseEntity<?> joinOwner(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Errors errors) {
        ownerService.joinOwner(requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/login/owner")
    public ResponseEntity<?> loginOwner(@RequestBody @Valid UserRequest.LoginDTO requestDTO, Errors errors) {
        UserResponse.LoginResponse response = ownerService.loginOwner(requestDTO);

        return ResponseEntity.ok().header(JWTProvider.HEADER, response.getJwtToken()).body(ApiUtils.success(null));
    }
}

