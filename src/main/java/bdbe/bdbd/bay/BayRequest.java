package bdbe.bdbd.bay;

import bdbe.bdbd.carwash.Carwash;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

public class BayRequest {

    @Getter
    @Setter
    @ToString
    public static class SaveDTO {

        @NotNull(message = "Bay number is required.")
        private Integer bayNum;

        public Bay toBayEntity(Carwash carwash) {
            return Bay.builder()
                    .carwash(carwash)
                    .bayNum(bayNum)
                    .status(1) // 활성화 상태로 생성
                    .build();
        }

    }

    @Getter
    @Setter
    @ToString
    public static class LocationDTO {
        private String placeName;
        private String address;
        private double latitude;
        private double longitude;

    }

    @Getter
    @Setter
    public static class OperatingTimeDTO {
        private TimeSlot weekday;
        private TimeSlot weekend;

        @Getter
        @Setter
        public static class TimeSlot {
            private LocalTime start;
            private LocalTime end;

        }
    }
}