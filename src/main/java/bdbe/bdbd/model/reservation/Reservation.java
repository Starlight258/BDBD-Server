package bdbe.bdbd.model.reservation;

import bdbe.bdbd.model.bay.Bay;
import bdbe.bdbd.model.carwash.Carwash;
import bdbe.bdbd.model.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Getter
@NoArgsConstructor
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(nullable = false)
    private int price;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_id", nullable = false)
    private Bay bay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "m_id", nullable = false)
    private Member member;


    @Builder
    public Reservation(Long id, int price, LocalDateTime startTime, LocalDateTime endTime, Bay bay, Member member) {
        this.id = id;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bay = bay;
        this.member = member;
    }

    public void updateReservation(LocalDateTime startTime, LocalDateTime endTime, Carwash carwash) {
        this.startTime = startTime;
        this.endTime = endTime;

        int perPrice = carwash.getPrice();
        int minutesDifference = (int) ChronoUnit.MINUTES.between(startTime, endTime);
        int blocksOf30Minutes = minutesDifference / 30;
        this.price = perPrice * blocksOf30Minutes;

    }

    public void changeDeletedFlag(boolean flag) {
        this.isDeleted = flag;
    }


}