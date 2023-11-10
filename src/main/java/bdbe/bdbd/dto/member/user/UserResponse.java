package bdbe.bdbd.dto.member.user;

import bdbe.bdbd.model.member.Member;
import lombok.Getter;
import lombok.Setter;

public class UserResponse {
    @Getter
    @Setter
    public static class FindById {
        private long id;
        private String username;
        private String email;

        public FindById(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
            this.email = member.getEmail();
        }
    }

    @Getter
    @Setter
    public static class LoginResponse {
        private String jwtToken;
        private String redirectUrl;

        public LoginResponse(String jwtToken, String redirectUrl) {
            this.jwtToken = jwtToken;
            this.redirectUrl = redirectUrl;
        }
    }
}
