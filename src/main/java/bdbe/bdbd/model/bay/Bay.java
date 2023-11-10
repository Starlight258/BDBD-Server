package bdbe.bdbd.model.bay;

import bdbe.bdbd._core.exception.BadRequestError;
import bdbe.bdbd.model.carwash.Carwash;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "bay")
public class Bay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "bay_num", nullable = false)
    private int bayNum;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name = "w_id", nullable = false)
    private Carwash carwash;

    @Column(name = "status", nullable = false)
    private int status;

    @Builder
    public Bay(Long id, int bayNum, Carwash carwash, int status) {
        this.id = id;
        this.bayNum = bayNum;
        this.carwash = carwash;
        this.status = status;
    }

    public void changeStatus(int newStatus) {
        if (newStatus != 0 && newStatus != 1) {
            throw new BadRequestError(
                    BadRequestError.ErrorCode.VALIDATION_FAILED,
                    Collections.singletonMap("Status", "Invalid status value: " + newStatus)
            );
        }
        this.status = newStatus;
    }
}