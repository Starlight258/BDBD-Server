package bdbe.bdbd.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class MemberRequest {
    @Getter
    @Setter
    public static class JoinDTO {

        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Please enter a valid email address.")
        private String email;

        @NotEmpty
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "It must contain letters, numbers, and special characters, and cannot contain spaces.")
        private String password;

        @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters.")
        @NotEmpty
        private String username;

        @Size(min = 9, max = 14)
        @NotEmpty
        private String tel;

        public Member toUserEntity(String encodedPassword) {
            return Member.builder()
                    .email(email)
                    .password(encodedPassword)
                    .username(username)
                    .role(MemberRole.ROLE_USER)
                    .tel(tel)
                    .build();
        }

        public Member toOwnerEntity(String encodedPassword) {
            return Member.builder()
                    .email(email)
                    .password(encodedPassword)
                    .username(username)
                    .role(MemberRole.ROLE_OWNER)
                    .tel(tel)
                    .build();
        }

    }


    @Getter
    @Setter
    public static class LoginDTO {
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Please enter a valid email address.")
        private String email;

        @NotEmpty
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "It must contain letters, numbers, and special characters, and cannot contain spaces.")
        private String password;
    }

    @Getter
    @Setter
    public static class EmailCheckDTO {
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Please enter a valid email address.")
        private String email;
    }

    @Getter
    @Setter
    public static class UpdatePasswordDTO {
        @NotEmpty
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "It must contain letters, numbers, and special characters, and cannot contain spaces.")
        private String password;
    }

}



