package bdbe.bdbd.controller.open;


import bdbe.bdbd._core.exception.BadRequestError;
import bdbe.bdbd._core.exception.UnAuthorizedError;
import bdbe.bdbd._core.security.JWTProvider;
import bdbe.bdbd._core.utils.ApiUtils;
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
import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/open")
public class OpenMemberController {

    private final UserService userService;
    private final OwnerService ownerService;

    @PostMapping("/member/check")
    public ResponseEntity<?> check(@RequestBody @Valid UserRequest.EmailCheckDTO emailCheckDTO, Errors errors) {
        userService.sameCheckEmail(emailCheckDTO.getEmail());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/join/user")
    public ResponseEntity<?> joinUser(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Errors errors) {
        userService.join(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/login/user")
    public ResponseEntity<?> loginUser(@RequestBody @Valid UserRequest.LoginDTO requestDTO, Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
            throw new UnAuthorizedError(
            UnAuthorizedError.ErrorCode.ACCESS_DENIED,
                    Collections.singletonMap("Token", errorMessage)
            );
        }
        UserResponse.LoginResponse response = userService.login(requestDTO);
        return ResponseEntity.ok().header(JWTProvider.HEADER, response.getJwtToken()).body(ApiUtils.success(null));
    }

    @PostMapping("/join/owner")
    public ResponseEntity<?> joinOwner(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Errors errors) {
        ownerService.join(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/login/owner")
    public ResponseEntity<?> loginOwner(@RequestBody @Valid UserRequest.LoginDTO requestDTO, Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
            throw new UnAuthorizedError(
                    UnAuthorizedError.ErrorCode.ACCESS_DENIED,
                    Collections.singletonMap("Token", errorMessage)
            );
        }
        UserResponse.LoginResponse response = ownerService.login(requestDTO);
        return ResponseEntity.ok().header(JWTProvider.HEADER, response.getJwtToken()).body(ApiUtils.success(null));
    }
}

