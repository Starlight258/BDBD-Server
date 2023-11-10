package bdbe.bdbd.service.member;


import bdbe.bdbd._core.exception.BadRequestError;
import bdbe.bdbd._core.exception.InternalServerError;
import bdbe.bdbd._core.exception.NotFoundError;
import bdbe.bdbd._core.exception.UnAuthorizedError;
import bdbe.bdbd._core.security.JWTProvider;
import bdbe.bdbd.dto.member.owner.OwnerResponse;
import bdbe.bdbd.dto.member.user.UserRequest;
import bdbe.bdbd.dto.member.user.UserResponse;
import bdbe.bdbd.model.member.Member;
import bdbe.bdbd.repository.member.MemberJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final MemberJPARepository memberJPARepository;

    @Transactional
    public void join(UserRequest.JoinDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());

        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        try {
            memberJPARepository.save(requestDTO.toUserEntity(encodedPassword));
        } catch (Exception e) {
            throw new InternalServerError(
                    InternalServerError.ErrorCode.INTERNAL_SERVER_ERROR,
                    Collections.singletonMap("error", "Unknown server error occurred."));
        }
    }

    public UserResponse.LoginResponse login(UserRequest.LoginDTO requestDTO) {
        Member memberPS = memberJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
                () -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("Email", "email not found : " + requestDTO.getEmail())
                ));

        if (!passwordEncoder.matches(requestDTO.getPassword(), memberPS.getPassword())) {
            throw new UnAuthorizedError(
                    UnAuthorizedError.ErrorCode.AUTHENTICATION_FAILED,
                    Collections.singletonMap("Password", "Wrong password")
            );
        }

        String jwt = JWTProvider.create(memberPS);
        String redirectUrl = "/user/home";

        return new UserResponse.LoginResponse(jwt, redirectUrl);
    }


    public void sameCheckEmail(String email) {
        Optional<Member> userOP = memberJPARepository.findByEmail(email);
        if (userOP.isPresent()) {
            throw new BadRequestError(
                    BadRequestError.ErrorCode.DUPLICATE_RESOURCE,
                    Collections.singletonMap("Email", "Duplicate email exist : " + email));
        }
    }

    /*
        토큰으로 전달받은 Member 객체의 ID를 이용하여 데이터베이스에서 해당 멤버의 전체 정보를 조회하는 메서드
     */
    public OwnerResponse.UserInfoDTO findUserInfo(Member member) {
        Member findMember = memberJPARepository.findById(member.getId())
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("MemberId", "Member is not found.")
                ));

        return new OwnerResponse.UserInfoDTO(findMember);
    }
}