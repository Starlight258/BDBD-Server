package bdbe.bdbd.member;

import bdbe.bdbd._core.errors.utils.DateUtils;
import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.file.File;
import bdbe.bdbd.optime.DayType;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.reservation.Reservation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OwnerResponse {

    @Getter
    @Setter
    @ToString
    public static class SaleResponseDTO {
        private List<CarwashListDTO> carwashList;  // 세차장 목록
        private List<ReservationCarwashDTO> reservationList; // 매출 정보

        public SaleResponseDTO(List<Carwash> carwashList, List<Reservation> reservationList) {
            this.carwashList = carwashList.stream()
                    .map(CarwashListDTO::new)
                    .collect(Collectors.toList());
            this.reservationList = reservationList.stream()
                    .map(ReservationCarwashDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class CarwashListDTO {
        private Long carwashId;
        private String name;

        public CarwashListDTO(Carwash carwash) {
            this.carwashId = carwash.getId();
            this.name = carwash.getName();
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ReservationCarwashDTO {
        private ReservationDTO reservation;
        private CarwashDTO carwash;

        public ReservationCarwashDTO(Reservation reservation) {
            this.reservation = new ReservationDTO(reservation);
            this.carwash = new CarwashDTO(reservation.getBay().getCarwash());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ReservationListDTO {
        List<ReservationDTO> reservationDTOList;

        public ReservationListDTO(List<Reservation> reservationList) {
            this.reservationDTOList = reservationList.stream()
                    .map(ReservationDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ReservationDTO {
        private Long reservationId;
        private Long bayId;
        private int bayNo;
        private String nickname;
        private int totalPrice;
        private String startTime;
        private String endTime;

        public ReservationDTO(Reservation reservation) {
            this.reservationId = reservation.getId();
            this.bayId = reservation.getBay().getId();
            this.bayNo = reservation.getBay().getBayNum();
            this.nickname = reservation.getMember().getUsername();
            this.totalPrice = reservation.getPrice();
            this.startTime = DateUtils.formatDateTime(reservation.getStartTime());
            this.endTime = DateUtils.formatDateTime(reservation.getEndTime());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class CarwashDTO {
        private Long carwashId;
        private String name;

        public CarwashDTO(Carwash carwash) {
            this.carwashId = carwash.getId();
            this.name = carwash.getName();
        }
    }

    @Getter
    @Setter
    public static class FileDTO {
        private Long id;
        private String name;
        private String url;
        private LocalDateTime uploadedAt;

        public FileDTO(File file) {
            this.id = file.getId();
            this.name = file.getName();
            this.url = file.getUrl();
            this.uploadedAt = file.getUploadedAt();
        }
    }


    @Getter
    @Setter
    @ToString
    public static class ReservationOverviewResponseDTO {
        private List<CarwashManageByOwnerDTO> carwash = new ArrayList<>();

        public void addCarwashManageByOwnerDTO(CarwashManageByOwnerDTO carwashManageByOwnerDTO) {
            this.carwash.add(carwashManageByOwnerDTO);
        }
    }

    @Getter
    @Setter
    @ToString
    public static class CarwashManageByOwnerDTO { // 매장 관리 (owner별, 세차장별 )
        private Long id;
        private String name;
        private OperationTimeDTO optime;
        private List<BayReservationDTO> bays = new ArrayList<>();
        private List<FileDTO> imageFiles;

        public CarwashManageByOwnerDTO(Carwash carwash, List<Bay> bayList, List<Optime> optimeList, List<Reservation> reservationList, List<File> files) {
            this.id = carwash.getId();
            this.name = carwash.getName();
            this.optime = new OperationTimeDTO(optimeList);
            for (Bay bay : bayList) {
                BayReservationDTO bayReservationDTO = new BayReservationDTO(bay, reservationList);
                this.bays.add(bayReservationDTO);
            }
            this.imageFiles = files.stream().map(FileDTO::new).collect(Collectors.toList());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class CarwashManageDTO { // 매장 관리 (owner별, 세차장별 )
        private Long id;
        private String name;
        private Long monthlySales;
        private Long monthlyReservations;
        private OperationTimeDTO optime;
        private List<BayReservationDTO> bays = new ArrayList<>();
        private FileDTO image;

        public CarwashManageDTO(Carwash carwash, Long monthlySales, Long monthlyReservations, List<Bay> bayList, List<Optime> optimeList, List<Reservation> reservationList, File file) {
            this.id = carwash.getId();
            this.name = carwash.getName();
            this.monthlySales = monthlySales;
            this.monthlyReservations = monthlyReservations;
            this.optime = new OperationTimeDTO(optimeList);
            for (Bay bay : bayList) {
                BayReservationDTO bayReservationDTO = new BayReservationDTO(bay, reservationList);
                this.bays.add(bayReservationDTO);
            }
            if (file != null && !file.isDeleted()) {
                this.image = new FileDTO(file);
            } else {
                this.image = null;
            }
        }
    }

    @Getter
    @Setter
    @ToString
    public static class OperationTimeDTO {
        private TimeFrameDTO weekday;
        private TimeFrameDTO weekend;

        public OperationTimeDTO(List<Optime> optimeList) {
            optimeList.forEach(optime -> {
                if (optime.getDayType() == DayType.WEEKDAY) {
                    this.weekday = new TimeFrameDTO(optime);
                } else if (optime.getDayType() == DayType.WEEKEND) {
                    this.weekend = new TimeFrameDTO(optime);
                }
            });
        }
    }

    @Getter
    @Setter
    @ToString
    public static class TimeFrameDTO {
        private LocalTime start;
        private LocalTime end;

        public TimeFrameDTO(Optime optime) {
            this.start = optime.getStartTime();
            this.end = optime.getEndTime();
        }
    }

    @Getter
    @Setter
    @ToString
    public static class BayReservationDTO {
        private Long bayId;
        private int bayNo;
        private int status;
        private List<BookedTimeDTO> bayBookedTime;

        public BayReservationDTO(Bay bay, List<Reservation> reservationList) {
            this.bayId = bay.getId();
            this.bayNo = bay.getBayNum();
            this.status = bay.getStatus();
            this.bayBookedTime = reservationList.stream()
                    .filter(reservation -> reservation.getBay() != null && reservation.getBay().getId().equals(bay.getId()))
                    .map(BookedTimeDTO::new)
                    .collect(Collectors.toList());

        }


        @Getter
        @Setter
        @ToString
        public static class BookedTimeDTO {
            private String start;
            private String end;

            public BookedTimeDTO(Reservation reservation) {
                this.start = DateUtils.formatDateTime(reservation.getStartTime());
                this.end = DateUtils.formatDateTime(reservation.getEndTime());
            }
        }
    }

    @Getter
    @Setter
    public static class OwnerDashboardDTO {
        private Long monthlySales;
        private double salesGrowthPercentage;
        private Long monthlyReservations;
        private double reservationGrowthPercentage;
        private List<CarwashInfoDTO> myStores;

        public OwnerDashboardDTO(Long monthlySales, double salesGrowthPercentage, Long monthlyReservations, double reservationGrowthPercentage, List<CarwashInfoDTO> myStores) {
            this.monthlySales = monthlySales;
            this.salesGrowthPercentage = salesGrowthPercentage;
            this.monthlyReservations = monthlyReservations;
            this.reservationGrowthPercentage = reservationGrowthPercentage;
            this.myStores = myStores;
        }

    }

    @Getter
    @Setter
    public static class CarwashInfoDTO {
        private Long carwashId;
        private String name;
        private Long monthlySales;
        private Long monthlyReservations;
        private List<FileDTO> imageFiles;


        public CarwashInfoDTO(Carwash carwash, Long monthlySales, Long monthlyReservations, List<File> files) {
            this.carwashId = carwash.getId();
            this.name = carwash.getName();
            this.monthlySales = monthlySales;
            this.monthlyReservations = monthlyReservations;
            this.imageFiles = files.stream()
                    .map(FileDTO::new)
                    .collect(Collectors.toList());

        }
    }

    @Getter
    @Setter
    @ToString
    public static class ReservationCarwashListDTO {
        private List<ReservationCarwashDTO> reservationList;

        public ReservationCarwashListDTO(List<Reservation> reservationList) {
            this.reservationList = reservationList.stream().map(ReservationCarwashDTO::new).collect(Collectors.toList());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class UserInfoDTO {
        private Long id;
        private String name;

        public UserInfoDTO(Member member) {
            this.id = member.getId();
            this.name = member.getUsername();
        }
    }


}
