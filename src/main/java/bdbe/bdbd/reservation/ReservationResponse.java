package bdbe.bdbd.reservation;

import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.file.File;
import bdbe.bdbd.location.Location;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationResponse {
    @Getter
    @Setter
    @ToString
    public static class findAllResponseDTO {
        private List<BayResponseDTO> bayList;

        public findAllResponseDTO(List<Bay> bayList, List<Reservation> reservationList) {
            this.bayList = bayList.stream()
                    .map(bay -> {
                        BayResponseDTO bayResponseDTO = new BayResponseDTO();
                        bayResponseDTO.setBayId(bay.getId());
                        bayResponseDTO.setBayNo(bay.getBayNum());
                // 해당 베이의 예약 모두 담기
                List<BookedTimeDTO> bookedTimes = reservationList.stream()
                        .filter(reservation -> reservation.getBay().getId().equals(bay.getId()))
                        .map(reservation -> {
                            BookedTimeDTO bookedTimeDTO = new BookedTimeDTO();
                            bookedTimeDTO.setStartTime(reservation.getStartTime());
                            bookedTimeDTO.setEndTime(reservation.getEndTime());
                            return bookedTimeDTO;
                        }).collect(Collectors.toList());
                bayResponseDTO.setBayBookedTime(bookedTimes);
                return bayResponseDTO;
            }).collect(Collectors.toList());
        }

        @Getter
        @Setter
        @ToString
        public static class BayResponseDTO {
            private Long bayId;
            private int bayNo;
            private List<BookedTimeDTO> bayBookedTime;
        }
        @Getter
        @Setter
        @ToString
        public static class BookedTimeDTO{
            private LocalDateTime startTime;
            private LocalDateTime endTime;
        }

    }
    @Getter
    @Setter
    @ToString
    public static class findLatestOneResponseDTO {
        private ReservationDTO reservation;
        private CarwashDTO carwash;
        public findLatestOneResponseDTO(Reservation reservation, Bay bay, Carwash carwash, Location location, File carwashImage) {
            ReservationDTO reservationDTO = new ReservationDTO();
            TimeDTO timeDTO = new TimeDTO();
            timeDTO.start = reservation.getStartTime();
            timeDTO.end = reservation.getEndTime();
            reservationDTO.time = timeDTO;
            reservationDTO.price = reservation.getPrice();
            reservationDTO.bayNo = bay.getBayNum();
            reservationDTO.reservationId = reservation.getId();
            this.reservation = reservationDTO;

            CarwashDTO carwashDTO = new CarwashDTO();
            carwashDTO.name = carwash.getName();
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.latitude = location.getLatitude();
            locationDTO.longitude = location.getLongitude();
            carwashDTO.location = locationDTO;
            carwashDTO.carwashImages = (carwashImage != null) ? Collections.singletonList(new ImageDTO(carwashImage)) : Collections.emptyList();
            this.carwash = carwashDTO;
        }
    }
    @Getter
    @Setter
    @ToString
    public static class ReservationDTO {
        private Long reservationId;
        private TimeDTO time;
        private int price;
        private int bayNo; // 예약된 베이 번호
    }
    @Getter
    @Setter
    @ToString
    public static class CarwashDTO {
        private String name;
        private LocationDTO location;
        private List<ImageDTO> carwashImages;
//        private String imagePath;
    }
    @Getter
    @Setter
    @ToString
    public static class TimeDTO{
        private LocalDateTime start;
        private LocalDateTime end;
    }
    @Getter
    @Setter
    @ToString
    public static class LocationDTO{
        private double latitude;
        private double longitude;
    }
    @Getter
    @Setter
    @ToString
    public static class ImageDTO {
        private Long id;
        private String name;
        private String url;
//        private String path;
        private LocalDateTime uploadedAt;

        public ImageDTO(File file) {
            this.id = file.getId();
            this.name = file.getName();
            this.url = file.getUrl();
//            this.path = file.getPath();
            this.uploadedAt = file.getUploadedAt();
        }
    }

    @Getter
    @Setter
    @ToString
    public static class fetchCurrentStatusReservationDTO {
        private List<ReservationInfoDTO> current;
        private List<ReservationInfoDTO> upcoming;
        private List<ReservationInfoDTO> completed;

        public fetchCurrentStatusReservationDTO(List<ReservationInfoDTO> current, List<ReservationInfoDTO> upcoming, List<ReservationInfoDTO> completed) {
            this.current = current;
            this.upcoming = upcoming;
            this.completed = completed;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class fetchRecentReservationDTO {
        private List<RecentReservation> recent;

        public fetchRecentReservationDTO(List<RecentReservation> recent) {
            this.recent = recent;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class RecentReservation {
        private Long carwashId;
        private ImageDTO image;
        private LocalDate date;
        private String carwashName;

        public RecentReservation(Reservation reservation, File file) {
            this.carwashId = reservation.getBay().getCarwash().getId();
            this.image = (file != null) ? new ImageDTO(file) : null;
            this.date = reservation.getStartTime().toLocalDate();
            this.carwashName = reservation.getBay().getCarwash().getName();
        }
    }




    @Getter
    @Setter
    public static class ReservationInfoDTO{
        private Long id; // 예약 id
        private TimeDTO time;
        private Long carwashId;
        private String carwashName;
        private int bayNum;
        private int price;
        private ImageDTO image;
        public ReservationInfoDTO(Reservation reservation, Bay bay, Carwash carwash) {
            this.id = reservation.getId();
            TimeDTO timeDTO = new TimeDTO();
            timeDTO.start = reservation.getStartTime();
            timeDTO.end = reservation.getEndTime();
            this.time = timeDTO;
            this.carwashId = carwash.getId();
            this.carwashName = carwash.getName();
            this.bayNum = bay.getBayNum();
            this.price = reservation.getPrice();
            List<File> activeFiles = carwash.getFileList().stream()
                    .filter(file -> !file.isDeleted())  // 삭제되지 않은 파일만 포함
                    .collect(Collectors.toList());
            if (!activeFiles.isEmpty()) {
                this.image = new ImageDTO(activeFiles.get(0));
            }
        }
    }

    @Getter
    @Setter
    @ToString
    public static class PayAmountDTO {

        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private int price;

        public PayAmountDTO(LocalDateTime startTime, LocalDateTime endTime, int price) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.price = price;
        }
    }

}
