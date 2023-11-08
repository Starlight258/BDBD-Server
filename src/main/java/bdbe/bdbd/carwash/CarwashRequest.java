package bdbe.bdbd.carwash;

import bdbe.bdbd.optime.DayType;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.location.Location;
import bdbe.bdbd.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CarwashRequest {

    @Getter
    @Setter
    @ToString
    public static class SaveDTO {

        @NotEmpty(message = "Name is required.")
        @Size(min = 2, max = 20, message = "Place name must be between 2 and 20 characters.")
        private String name;

        @NotNull(message = "Location is required.")
        private LocationDTO location;

        @NotEmpty( message = "Price id required.")
        private String price;


        private OperatingTimeDTO optime;

        private List<Long> keywordId;

        @NotEmpty( message = "Description id required.")
        @Size(max = 200, message = "Description cannot be longer than 200 characters." )
        private String description;

        @NotEmpty
        @Size(min = 9, max = 14)
        private String tel;


        public Carwash toCarwashEntity(Location location, Member member) {
            return Carwash.builder()
                    .name(name)
                    .rate(0)
                    .tel(tel)
                    .des(description)
                    .price(Integer.parseInt(price))  // 문자열 price를 int로 변환
                    .location(location)
                    .member(member)
                    .build();
        }

        public Location toLocationEntity() {

            return Location.builder()
                    .place(location.placeName)
                    .address(location.address)
                    .latitude(location.latitude)
                    .longitude(location.longitude)
                    .build();
        }

        public List<Optime> toOptimeEntities(Carwash carwash) {
            List<Optime> optimeList = new ArrayList<>();

            optimeList.add(Optime.builder()
                    .dayType(DayType.WEEKDAY)
                    .startTime(optime.getWeekday().getStart())
                    .endTime(optime.getWeekday().getEnd())
                    .carwash(carwash)
                    .build());

            optimeList.add(Optime.builder()
                    .dayType(DayType.WEEKEND)
                    .startTime(optime.getWeekend().getStart())
                    .endTime(optime.getWeekend().getEnd())
                    .carwash(carwash)
                    .build());

            return optimeList;
        }



        public String getFileExtension(String filename) {
            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex == -1) {
                return null;
            }
            return filename.substring(dotIndex + 1);
        }


    }

    @Getter
    @Setter
    @ToString
    public static class LocationDTO {

        @NotEmpty(message = "Place name is required.")
        @Size(min = 3, max = 30, message = "Place name must be between 3 and 30 characters.")
        private String placeName;

        @NotEmpty(message = "Address is required.")
        @Size(min = 5, max = 50, message = "Address must be between 5 and 200 characters.")
        private String address;

        @NotNull(message = "Latitude is required.")
        @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90.")
        @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90.")
        private double latitude;

        @NotNull(message = "Longitude is required.")
        @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180.")
        @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180.")
        private double longitude;

    }

    @Getter
    @Setter
    public static class OperatingTimeDTO {

        @NotNull(message = "Weekday is required.")
        private TimeSlot weekday;

        @NotNull(message = "Weekend is required")
        private TimeSlot weekend;

        @Getter
        @Setter
        public static class TimeSlot {

            @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Open time must be in the format HH:mm.")
            private LocalTime start;

            @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Close time must be in the format HH:mm.")
            private LocalTime end;

        }
    }

    @Getter
    @Setter
    public static class CarwashDistanceDTO {

        @NotNull(message = "ID is required.")
        private Long id;

        @NotEmpty(message = "Name is required.")
        @Size(min = 2, max = 20, message = "Place name must be between 2 and 20 characters.")
        private String name;

        @NotNull(message = "Location is required.")
        private Location location;

        @Positive(message = "Distance must be positive.")
        private double distance;

        @NotNull(message = "rate is required.")
        @DecimalMax(value = "5.0", message = "The rating cannot exceed 5 points.")
        private double rate;

        @NotNull(message = "Price is required.")
        private Integer price;

        public CarwashDistanceDTO(Long id, String name, Location location, double distance, double rate, int price) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.distance = distance;
            this.rate = rate;
            this.price = price;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class UserLocationDTO {

        @NotNull(message = "Latitude is required.")
        @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90.")
        @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90.")
        private double latitude;

        @NotNull(message = "Longitude is required.")
        @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180.")
        @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180.")
        private double longitude;
    }

    @Getter
    @Setter
    public static class SearchRequestDTO {

        @NotEmpty(message = "At least one keyword ID is required.")
        private List<Long> keywordIds;

        @NotNull(message = "Latitude is required.")
        @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90.")
        @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90.")
        private double latitude;

        @NotNull(message = "Longitude is required.")
        @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180.")
        @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180.")
        private double longitude;
    }

    @Getter
    @Setter
    @ToString
    public static class updateCarwashDetailsDTO {

        @NotEmpty(message = "Name is required.")
        @Size(min = 2, max = 20, message = "Place name must be between 2 and 20 characters.")
        private String name;

        @NotNull(message = "Price is required.")
        private Integer price;

        @NotEmpty
        @Size(min = 9, max = 14)
        private String tel;

        private updateLocationDTO locationDTO;

        private updateOperatingTimeDTO optime;

        @NotNull(message = "At least one keyword ID is required.")
        private List<Long> keywordId;

        @NotEmpty( message = "Description is required.")
        @Size(max = 200, message = "Description cannot be longer than 200 characters." )
        private String description;

    }
    @Getter
    @Setter
    public static class updateOperatingTimeDTO {

        @NotNull(message = "Weekday is required.")
        private CarwashRequest.updateOperatingTimeDTO.updateTimeSlot weekday;

        @NotNull(message = "Weekend is required")
        private CarwashRequest.updateOperatingTimeDTO.updateTimeSlot weekend;

        @Getter
        @Setter
        public static class updateTimeSlot {

            @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Open time must be in the format HH:mm.")
            private LocalTime start;

            @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Close time must be in the format HH:mm.")
            private LocalTime end;

        }
    }

    @Getter
    @Setter
    public static class updateLocationDTO {

        @NotEmpty(message = "Place name is required.")
        @Size(min = 3, max = 30, message = "Place name must be between 3 and 30 characters.")
        private String placeName;

        @NotEmpty(message = "Address is required.")
        @Size(min = 5, max = 50, message = "Address must be between 5 and 200 characters.")
        private String address;

        @NotNull(message = "Latitude is required.")
        @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90.")
        @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90.")
        private double latitude;

        @NotNull(message = "Longitude is required.")
        @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180.")
        @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180.")
        private double longitude;
    }
}