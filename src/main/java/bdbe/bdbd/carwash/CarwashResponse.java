package bdbe.bdbd.carwash;

import bdbe.bdbd.location.Location;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.file.File;
import bdbe.bdbd.reservation.ReservationResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class CarwashResponse {
    @Getter
    @Setter
    public static class FindAllDTO {
        private Long id;
        private String name;
        private double rate;
        private String tel;
        private String des;
        private int price;
        private Long lId;
        private Long userId;

        public FindAllDTO(Carwash carwash) {
            this.id = carwash.getId();
            this.name = carwash.getName();
            this.rate = carwash.getRate();
            this.tel = carwash.getTel();
            this.des = carwash.getDes();
            this.price = carwash.getPrice();
            this.lId = carwash.getLocation().getId();
            this.userId = carwash.getMember().getId();
        }
    }

    @Getter
    @Setter
    public class KeywordResponseDTO {
        private String keywordName;

        public KeywordResponseDTO(String keywordName) {
            this.keywordName = keywordName;
        }
    }



    @Getter
    @Setter
    public static class findByIdDTO {
        private Long id;
        private String name;
        private double rate;
        private int reviewCnt; // 리뷰 갯수
        private int bayCnt; // 예약 가능한 갯수
        private OperatingTimeDTOResponse optime; //
        private CarwashResponse.locationDTO locationDTO;
        private List<Long> keywordId;
        private String description;
        private String tel;
        private List<FileDTO> imageFiles;

        public findByIdDTO(Carwash carwash, int reviewCnt, int bayCnt, Location location, List<Long> keywordId, Optime weekOptime, Optime endOptime, List<File> files) {
            this.id = carwash.getId();
            this.name = carwash.getName();
            this.rate = carwash.getRate();
            this.reviewCnt = reviewCnt;
            this.bayCnt = bayCnt;
            this.optime = toOptimeListDTO(weekOptime, endOptime);
            this.locationDTO = toLocationDTO(location);
            this.keywordId = keywordId;
            this.description = carwash.getDes();
            this.tel = carwash.getTel();
            this.imageFiles = files.stream().filter(file -> !file.isDeleted()).map(FileDTO::new).collect(Collectors.toList());
        }

        public OperatingTimeDTOResponse toOptimeListDTO(Optime weekOptime, Optime endOptime) {
            OperatingTimeDTOResponse dto = new OperatingTimeDTOResponse();
            OperatingTimeDTOResponse.TimeSlotResponse weekSlot= new OperatingTimeDTOResponse.TimeSlotResponse();
            weekSlot.setStart(weekOptime.getStartTime());
            weekSlot.setEnd(weekOptime.getEndTime());
            dto.setWeekday(weekSlot);

            OperatingTimeDTOResponse.TimeSlotResponse endSlot= new OperatingTimeDTOResponse.TimeSlotResponse();
            endSlot.setStart(endOptime.getStartTime());
            endSlot.setEnd(endOptime.getEndTime());
            dto.setWeekend(endSlot);

            return dto;

        }

        public locationDTO toLocationDTO(Location location) {
            locationDTO locationDTO = new locationDTO();
            locationDTO.setAddress(location.getAddress());
            locationDTO.setPlaceName(location.getPlace());
            return locationDTO;
        }
    }
    @Getter
    @Setter
    public static class OperatingTimeDTOResponse {
        private TimeSlotResponse weekday;
        private TimeSlotResponse weekend;

        @Getter
        @Setter
        public static class TimeSlotResponse {
            private LocalTime start;
            private LocalTime end;

        }
    }

    @Getter
    @Setter
    public static class locationDTO {
        private String placeName;
        private String address;
        private double latitude;
        private double longitude;
    }

    @Getter
    @Setter
    public static class FileDTO{
        private Long id;
        private String name;
        private String url;
        public FileDTO(File file) {
            this.id = file.getId();
            this.name = file.getName();
            this.url = file.getUrl();
        }
    }

    @Getter
    @Setter
    public static class carwashDetailsDTO {
        private Long id;
        private String name;
        private int price;
        private String tel;
        private detailLocationDTO locationDTO;
        private detailsOperatingTimeDTO optime; //
        private List<Long> keywordId;
        private String description;
        private List<FileDTO> imageFiles;


        public carwashDetailsDTO(Carwash carwash, Location location, List<Long> keywordId, Optime weekOptime, Optime endOptime, List<File> files) {
            this.id = carwash.getId();
            this.name = carwash.getName();
            this.price = carwash.getPrice();
            this.tel = carwash.getTel();
            this.locationDTO = toLocationDTO(location);
            this.optime = toOptimeListDTO(weekOptime, endOptime);
            this.keywordId = keywordId;
            this.description = carwash.getDes();
            this.imageFiles = files.stream()
                    .filter(file -> !file.isDeleted())  // 삭제되지 않은 파일만 포함
                    .map(FileDTO::new)
                    .collect(Collectors.toList());
        }

        public detailsOperatingTimeDTO toOptimeListDTO(Optime weekOptime, Optime endOptime) {
            detailsOperatingTimeDTO dto = new detailsOperatingTimeDTO();
            detailsOperatingTimeDTO.detailsTimeSlot weekSlot= new detailsOperatingTimeDTO.detailsTimeSlot();
            weekSlot.setStart(weekOptime.getStartTime());
            weekSlot.setEnd(weekOptime.getEndTime());
            dto.setWeekday(weekSlot);

            detailsOperatingTimeDTO.detailsTimeSlot endSlot= new detailsOperatingTimeDTO.detailsTimeSlot();
            endSlot.setStart(endOptime.getStartTime());
            endSlot.setEnd(endOptime.getEndTime());
            dto.setWeekend(endSlot);

            return dto;

        }

        public detailLocationDTO toLocationDTO(Location location) {
            detailLocationDTO detailLocationDTO = new detailLocationDTO();
            detailLocationDTO.setAddress(location.getAddress());
            return detailLocationDTO;
        }
    }
    @Getter
    @Setter
    public static class detailsOperatingTimeDTO {
        private detailsTimeSlot weekday;
        private detailsTimeSlot weekend;

        @Getter
        @Setter
        public static class detailsTimeSlot {
            private LocalTime start;
            private LocalTime end;

        }
    }

    @Getter
    @Setter
    public static class detailLocationDTO {
        private String address;

    }

    @Getter
    @Setter
    @ToString
    public static class updateCarwashDetailsResponseDTO {
        private Long id;
        private String name;
        private int price;
        private String tel;
        private CarwashResponse.updateLocationDTO locationDTO;
        private CarwashResponse.updateOperatingTimeDTO optime;
        private List<Long> keywordId;
        private String description;
        private List<ReservationResponse.ImageDTO> images;
        public void updateCarwashPart(Carwash carwash) {
            this.id = carwash.getId();
            this.name = carwash.getName();
            this.price = carwash.getPrice();
            this.tel = carwash.getTel();
            this.images = carwash.getFileList().stream()
                    .filter(file -> !file.isDeleted())
                    .map(ReservationResponse.ImageDTO::new)
                    .collect(Collectors.toList());
        }

        public void updateOptimePart(Optime weekdayOptime, Optime weekendOptime) {
            CarwashResponse.updateOperatingTimeDTO dto = new CarwashResponse.updateOperatingTimeDTO();

            CarwashResponse.updateOperatingTimeDTO.updateTimeSlot weekSlot = new CarwashResponse.updateOperatingTimeDTO.updateTimeSlot();
            weekSlot.setStart(weekdayOptime.getStartTime());
            weekSlot.setEnd(weekdayOptime.getEndTime());
            dto.setWeekday(weekSlot);

            CarwashResponse.updateOperatingTimeDTO.updateTimeSlot endSlot = new CarwashResponse.updateOperatingTimeDTO.updateTimeSlot();
            endSlot.setStart(weekendOptime.getStartTime());
            endSlot.setEnd(weekendOptime.getEndTime());
            dto.setWeekend(endSlot);
            this.optime = dto;

        }

        public void updateKeywordPart(List<Long> keywordId) {
            this.keywordId = keywordId;
        }

        public void updateLocationPart(Location location) {
            CarwashResponse.updateLocationDTO dto = new CarwashResponse.updateLocationDTO();
            dto.setPlaceName(location.getPlace());
            dto.setAddress(location.getAddress());
            dto.setLatitude(location.getLatitude());
            dto.setLongitude(location.getLongitude());
            this.locationDTO = dto;
        }
    }
    @Getter
    @Setter
    public static class updateOperatingTimeDTO {
        private CarwashResponse.updateOperatingTimeDTO.updateTimeSlot weekday;
        private CarwashResponse.updateOperatingTimeDTO.updateTimeSlot weekend;

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
