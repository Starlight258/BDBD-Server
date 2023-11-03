package bdbe.bdbd.carwash;

import bdbe.bdbd.optime.DayType;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.location.Location;
import bdbe.bdbd.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CarwashRequest {

    @Getter
    @Setter
    @ToString
    public static class SaveDTO {

        @NotEmpty
        private String name;
        @NotNull
        private LocationDTO location;

        private String price;

        private OperatingTimeDTO optime;
//        private List<FileRequest.FileDTO> images;
//        private List<MultipartFile> images;

        private List<Long> keywordId;
        private String description;
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

    @Getter
    @Setter
    public static class CarwashDistanceDTO {

        private Long id;
        private String name;
        private Location location;
        private double distance;
        private double rate;
        private int price;

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
        private double latitude;
        private double longitude;
    }

    @Getter
    @Setter
    public static class SearchRequestDTO {
        private List<Long> keywordIds;
        private double latitude;
        private double longitude;
    }

    @Getter
    @Setter
    @ToString
    public static class updateCarwashDetailsDTO {

        private String name;
        private int price;
        private String tel;
        private updateLocationDTO locationDTO;
        private updateOperatingTimeDTO optime;
        private List<Long> keywordId;
        private String description;

    }
    @Getter
    @Setter
    public static class updateOperatingTimeDTO {
        private CarwashRequest.updateOperatingTimeDTO.updateTimeSlot weekday;
        private CarwashRequest.updateOperatingTimeDTO.updateTimeSlot weekend;

        @Getter
        @Setter
        public static class updateTimeSlot {
            private LocalTime start;
            private LocalTime end;

        }
    }

    @Getter
    @Setter
    public static class updateLocationDTO {
        private String placeName;
        private String address;
        private double latitude;
        private double longitude;
    }
}