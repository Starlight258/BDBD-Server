package bdbe.bdbd.member;



import bdbe.bdbd._core.errors.exception.BadRequestError;
import bdbe.bdbd._core.errors.exception.InternalServerError;
import bdbe.bdbd._core.errors.security.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberJPARepository memberJPARepository;

    @Transactional
    public void join(MemberRequest.JoinDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());

        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        try {
            memberJPARepository.save(requestDTO.toUserEntity(encodedPassword));
        } catch (Exception e) {
            throw new InternalServerError("unknown server error");
        }
    }

//    public String login(UserRequest.LoginDTO requestDTO) {
//        User userPS = userJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
//                () -> new Exception400("이메일을 찾을 수 없습니다 : "+requestDTO.getEmail())
//        );
//
//        if(!passwordEncoder.matches(requestDTO.getPassword(), userPS.getPassword())){
//            throw new Exception400("패스워드가 잘못입력되었습니다.");
//        }
//        return JWTProvider.create(userPS);
//    }

    public MemberResponse.LoginResponse login(MemberRequest.LoginDTO requestDTO) {
        Member memberPS = memberJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
                () -> new BadRequestError("email not found : " + requestDTO.getEmail())
        );

        if (!passwordEncoder.matches(requestDTO.getPassword(), memberPS.getPassword())) {
            throw new BadRequestError("wrong password");
        }

        String jwt = JWTProvider.create(memberPS);
        String redirectUrl = "/user/home";

        return new MemberResponse.LoginResponse(jwt, redirectUrl);
    }


    public void sameCheckEmail(String email) {
        Optional<Member> userOP = memberJPARepository.findByEmail(email);
        if (userOP.isPresent()) {
            throw new BadRequestError("duplicate email exist : " + email);
        }
    }
}