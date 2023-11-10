package bdbe.bdbd.model.review;


import bdbe.bdbd.model.carwash.Carwash;
import bdbe.bdbd.model.member.Member;
import bdbe.bdbd.model.reservation.Reservation;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Data
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "m_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_id", nullable = false)
    private Carwash carwash;

    @OneToOne
    @JoinColumn(name = "r_id", nullable = false)
    private Reservation reservation;

    @Column(length = 100, nullable = false)
    private String comment;

    @Column(nullable = false)
    private double rate;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @Builder
    public Review(Long id, Member member, Carwash carwash, Reservation reservation, String comment, double rate) {
        this.id = id;
        this.member = member;
        this.carwash = carwash;
        this.reservation = reservation;
        this.comment = comment;
        this.rate = rate;
    }
}


