package bdbe.bdbd._core.utils;

import bdbe.bdbd._core.exception.BadRequestError;
import bdbe.bdbd.model.member.Member;
import bdbe.bdbd.repository.member.MemberJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberUtils {
    private final MemberJPARepository memberJPARepository;

    public void checkSameEmail(String email) {
        Optional<Member> memberOptional = memberJPARepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            throw new BadRequestError(BadRequestError.ErrorCode.DUPLICATE_RESOURCE, Collections.singletonMap("Email", "Duplicate email exist : " + email));
        }
    }
}
