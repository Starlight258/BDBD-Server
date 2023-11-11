package bdbe.bdbd.service.member;


import bdbe.bdbd._core.exception.InternalServerError;
import bdbe.bdbd._core.exception.NotFoundError;
import bdbe.bdbd._core.exception.UnAuthorizedError;
import bdbe.bdbd._core.security.JWTProvider;
import bdbe.bdbd._core.utils.MemberUtils;
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

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final MemberJPARepository memberJPARepository;
    private final MemberUtils memberUtils;

    @Transactional
    public void join(UserRequest.JoinDTO requestDTO) {
        memberUtils.checkSameEmail(requestDTO.getEmail());

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
        Member member = memberJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
                () -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("Email", "email not found : " + requestDTO.getEmail())
                ));

        if (!passwordEncoder.matches(requestDTO.getPassword(), member.getPassword())) {
            throw new UnAuthorizedError(
                    UnAuthorizedError.ErrorCode.AUTHENTICATION_FAILED,
                    Collections.singletonMap("Password", "Wrong password")
            );
        }

        String jwt = JWTProvider.create(member);
        String redirectUrl = "/user/home";

        return new UserResponse.LoginResponse(jwt, redirectUrl);
    }


    public OwnerResponse.UserInfoDTO findUserInfo(Member member) {
        Member findMember = memberJPARepository.findById(member.getId())
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("MemberId", "Member is not found.")
                ));

        return new OwnerResponse.UserInfoDTO(findMember);
    }
}