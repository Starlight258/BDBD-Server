package bdbe.bdbd.reservation;

import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ReservationRequest {

    @Getter
    @Setter
    @ToString
    public static class ReservationTimeDTO {

        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Open time must be in the format HH:mm.")
        private LocalDateTime startTime;

        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Close time must be in the format HH:mm.")
        private LocalDateTime endTime;
    }

    @Getter
    @Setter
    @ToString
    public static class SaveDTO {

        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Open time must be in the format HH:mm.")
        private LocalDateTime startTime;

        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Close time must be in the format HH:mm.")
        private LocalDateTime endTime;

        public Reservation toReservationEntity(Carwash carwash, Bay bay, Member member) {
            int perPrice = carwash.getPrice();
            LocalDateTime startTime = this.startTime;
            LocalDateTime endTime = this.endTime;

            int minutesDifference = (int) ChronoUnit.MINUTES.between(startTime, endTime); //시간 차 계산
            int blocksOf30Minutes = minutesDifference / 30; //30분 단위로 계산
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

        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Open time must be in the format HH:mm.")
        private LocalDateTime startTime;

        @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Close time must be in the format HH:mm.")
        private LocalDateTime endTime;

    }
}