package bdbe.bdbd.dto.bay;

import bdbe.bdbd.model.bay.Bay;
import bdbe.bdbd.model.carwash.Carwash;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.time.LocalTime;

public class BayRequest {

    @Getter
    @Setter
    @ToString
    public static class SaveDTO {

        @NotNull(message = "Bay number is required")
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

        @NotBlank(message = "Address is required")
        @Size(min = 5, max = 50, message = "Address must be between 5 and 200 characters")
        private String address;

        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90")
        @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90")
        private double latitude;

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180")
        @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180")
        private double longitude;

    }

    @Getter
    @Setter
    public static class OperatingTimeDTO {

        @NotNull(message = "Weekday is required")
        private TimeSlot weekday;

        @NotNull(message = "Weekend is required")
        private TimeSlot weekend;

        @Getter
        @Setter
        public static class TimeSlot {

            @NotNull(message = "Start time is required")
            private LocalTime start;

            @NotNull(message = "End time is required")
            private LocalTime end;

        }
    }
}