package bdbe.bdbd.model.member;

import bdbe.bdbd.model.Code.MemberRole;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private MemberRole role;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 45, nullable = false)
    private String username;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String tel;


    @Builder
    public Member(Long id, String email, String password, String username, MemberRole role, String tel) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.tel = tel;
    }
}
