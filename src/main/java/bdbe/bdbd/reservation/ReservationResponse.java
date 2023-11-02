package bdbe.bdbd.reservation;

import bdbe.bdbd._core.errors.utils.Haversine;
import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.file.File;
import bdbe.bdbd.location.Location;
import bdbe.bdbd.member.OwnerResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        public findLatestOneResponseDTO(Reservation reservation, Bay bay, Carwash carwash, Location location, List<File> carwashImages) {
            ReservationDTO reservationDTO = new ReservationDTO();

            TimeDTO timeDTO = new TimeDTO();
            timeDTO.start = reservation.getStartTime();
            timeDTO.end = reservation.getEndTime();
            reservationDTO.time = timeDTO;
            reservationDTO.price = reservation.getPrice();
            reservationDTO.bayNo = bay.getBayNum();
            this.reservation = reservationDTO;

            CarwashDTO carwashDTO = new CarwashDTO();
            carwashDTO.name = carwash.getName();
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.latitude = location.getLatitude();
            locationDTO.longitude = location.getLongitude();
            carwashDTO.location = locationDTO;
            carwashDTO.carwashImages = carwashImages.stream()
                    .map(ImageDTO::new)
                    .collect(Collectors.toList());
            this.carwash = carwashDTO;
        }
    }
    @Getter
    @Setter
    @ToString
    public static class ReservationDTO {
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
        private String path;
        private LocalDateTime uploadedAt;

        public ImageDTO(File file) {
            this.id = file.getId();
            this.name = file.getName();
            this.url = file.getUrl();
            this.path = file.getPath();
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

        public fetchRecentReservationDTO(List<Reservation> reservationList) {
            this.recent = reservationList.stream()
                    .map(RecentReservation::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class RecentReservation {
        private Long carwashId;
//        private String image;
        private LocalDate date;
        private String carwashName;

        public RecentReservation(Reservation reservation) {
            this.carwashId = reservation.getBay().getCarwash().getId();
//            this.image = image;
            this.date = reservation.getStartTime().toLocalDate();
            this.carwashName = reservation.getBay().getCarwash().getName();
        }
    }




    @Getter
    @Setter
    public static class ReservationInfoDTO{
        private Long id; // 예약 id
        private TimeDTO time;
        private String carwashName;
        private int bayNum;
        private int price;
//        private String image;
        public ReservationInfoDTO(Reservation reservation, Bay bay, Carwash carwash) {
            this.id = reservation.getId();
            TimeDTO timeDTO = new TimeDTO();
            timeDTO.start = reservation.getStartTime();
            timeDTO.end = reservation.getEndTime();
            this.time = timeDTO;
            this.carwashName = carwash.getName();
            this.bayNum = bay.getBayNum();
            this.price = reservation.getPrice();
        }
    }

}
