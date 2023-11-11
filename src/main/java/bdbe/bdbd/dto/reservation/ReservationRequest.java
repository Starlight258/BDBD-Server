package bdbe.bdbd.dto.reservation;

import bdbe.bdbd.model.bay.Bay;
import bdbe.bdbd.model.carwash.Carwash;
import bdbe.bdbd.model.member.Member;
import bdbe.bdbd.model.reservation.Reservation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ReservationRequest {

    @Getter
    @Setter
    @ToString
    public static class ReservationTimeDTO {
        @NotNull(message = "Start time is required")
        private LocalDateTime startTime;

        @NotNull(message = "End time is required")
        private LocalDateTime endTime;
    }

    @Getter
    @Setter
    @ToString
    public static class SaveDTO {
        @NotNull(message = "BayId is required")
        private Long bayId;

        @NotNull(message = "Start time is required")
        private LocalDateTime startTime;

        @NotNull(message = "End time is required")
        private LocalDateTime endTime;

        public Reservation toReservationEntity(Carwash carwash, Bay bay, Member member) {
            LocalDateTime startTime = this.startTime;
            LocalDateTime endTime = this.endTime;

            int perPrice = carwash.getPrice();
            int minutesDifference = (int) ChronoUnit.MINUTES.between(startTime, endTime);
            int blocksOf30Minutes = minutesDifference / 30;
            int price = perPrice * blocksOf30Minutes;

            return Reservation.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .price(price)
                    .bay(bay)
                    .member(member)
                    .build();
        }
    }

    @Getter
    @Setter
    @ToString
    public static class UpdateDTO {
        @NotNull(message = "Start time is required")
        private LocalDateTime startTime;

        @NotNull(message = "End time is required")
        private LocalDateTime endTime;
    }
}