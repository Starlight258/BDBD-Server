package bdbe.bdbd.model.optime;

import bdbe.bdbd.model.Code.DayType;
import bdbe.bdbd.model.carwash.Carwash;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "optime")
public class Optime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_type", length = 10, nullable = false)
    private DayType dayType; // 요일명 (평일, 주말, 휴일)

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_id", nullable = false)
    private Carwash carwash;

    @Builder
    public Optime(Long id, DayType dayType, LocalTime startTime, LocalTime endTime, Carwash carwash) {
        this.id = id;
        this.dayType = dayType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.carwash = carwash;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
}