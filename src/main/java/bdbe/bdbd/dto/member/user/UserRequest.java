package bdbe.bdbd.dto.member.user;

import bdbe.bdbd.model.Code.MemberRole;
import bdbe.bdbd.model.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class UserRequest {
    @Getter
    @Setter
    public static class JoinDTO {
        @NotBlank(message = "email is required.")
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Please enter a valid email address")
        private String email;

        @NotBlank(message = "password is required.")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "It must contain letters, numbers, and special characters, and cannot contain spaces")
        private String password;

        @NotBlank(message = "username is required.")
        @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters")
        private String username;

        @NotBlank(message = "Tel is required.")
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
        @NotBlank(message = "email is required.")
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Please enter a valid email address.")
        private String email;

        @NotBlank(message = "password is required.")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "It must contain letters, numbers, and special characters, and cannot contain spaces.")
        private String password;
    }

    @Getter
    @Setter
    public static class EmailCheckDTO {
        @NotBlank(message = "email is required.")
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Please enter a valid email address")
        private String email;
    }

    @Getter
    @Setter
    public static class UpdatePasswordDTO {
        @NotBlank(message = "password is required.")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "It must contain letters, numbers, and special characters, and cannot contain spaces")
        private String password;
    }
}



