package bdbe.bdbd.member;


import bdbe.bdbd._core.errors.exception.BadRequestError;
import bdbe.bdbd._core.errors.exception.UnAuthorizedError;
import bdbe.bdbd._core.errors.security.CacheService;
import bdbe.bdbd._core.errors.security.JWTProvider;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class MemberRestController {

    private final MemberService memberService;
    @Autowired
    private CacheService cacheService;


    // (기능3) 이메일 중복체크
    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody @Valid MemberRequest.EmailCheckDTO emailCheckDTO, Errors errors) {
        memberService.sameCheckEmail(emailCheckDTO.getEmail());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    //(기능4) 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> joinUser(@RequestBody @Valid MemberRequest.JoinDTO requestDTO, Errors errors) {
//        requestDTO.setRole(MemberRole.ROLE_USER);
        memberService.join(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }



    // (기능5) 로그인
//    @PostMapping("/login")
////    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDTO requestDTO, Errors errors) {
////        String jwt = userService.login(requestDTO);
////        return ResponseEntity.ok().header(JWTProvider.HEADER, jwt).body(ApiUtils.success(null));
////    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDTO requestDTO, Errors errors) {
//        if (errors.hasErrors()) {
//            String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
//            throw new Exception400(errorMessage);
//        }
//        String jwt = userService.login(requestDTO);
//        return ResponseEntity.ok().header(JWTProvider.HEADER, jwt).body(ApiUtils.success(null));
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid MemberRequest.LoginDTO requestDTO, Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
            throw new BadRequestError(errorMessage);
        }
        MemberResponse.LoginResponse response = memberService.login(requestDTO);
        return ResponseEntity.ok().header(JWTProvider.HEADER, response.getJwtToken()).body(ApiUtils.success(null));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            MemberResponse.LogoutResponse response = cacheService.logout(token);
            if (response.isSuccess()) {
                return ResponseEntity.ok().body(ApiUtils.success("Logged out successfully"));
            } else {
                throw new UnAuthorizedError("Logout failed");
            }
        }
        throw new UnAuthorizedError("No token provided");
    }




}

