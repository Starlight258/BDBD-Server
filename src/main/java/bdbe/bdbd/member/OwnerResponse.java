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
    public static class ReservationDTO {
        private Long reservationId;
        private int bayNo;
        private String nickname;
        private int totalPrice;
        private String startTime;
        private String endTime;

        public ReservationDTO(Reservation reservation) {
            this.reservationId = reservation.getId();
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
    @ToString
    public static class ReservationOverviewResponseDTO {
        private List<CarwashManageDTO> carwash = new ArrayList<>();

        public void addCarwashManageDTO(CarwashManageDTO carwashManageDTO) {
            this.carwash.add(carwashManageDTO);
        }
    }

    @Getter
    @Setter
    @ToString
    public static class CarwashManageDTO { // 매장 관리 (owner별, 세차장별 )
        private Long id;
        private String name;
        private OperationTimeDTO optime;
        private List<BayReservationDTO> bays = new ArrayList<>();
        private List<FileDTO> imageFiles;

        public CarwashManageDTO(Carwash carwash, List<Bay> bayList, List<Optime> optimeList, List<Reservation> reservationList, List<File> files) {
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
        private int bayNo;
        private List<BookedTimeDTO> bayBookedTime;

        public BayReservationDTO(Bay bay, List<Reservation> reservationList) {
            this.bayNo = bay.getBayNum();
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
        private List<CarwashInfoDTO> myStores = new ArrayList<>();

        public OwnerDashboardDTO(Long monthlySales, double salesGrowthPercentage, Long monthlyReservations, double reservationGrowthPercentage, List<CarwashInfoDTO> myStores) {
            this.monthlySales = monthlySales;
            this.salesGrowthPercentage = salesGrowthPercentage;
            this.monthlyReservations = monthlyReservations;
            this.reservationGrowthPercentage = reservationGrowthPercentage;
            this.myStores = myStores;
        }
        public void addCarwashInfoDTO(CarwashInfoDTO carwashInfoDTO) {
            this.myStores.add(carwashInfoDTO);
        }
    }

    @Getter
    @Setter
    public static class CarwashInfoDTO {
        private String name;
        private Long monthlySales;
        private Long monthlyReservations;
        private List<FileDTO> imageFiles;


        public CarwashInfoDTO(Carwash carwash, Long monthlySales, Long monthlyReservations,List<File> files) {
            this.name = carwash.getName();
            this.monthlySales = monthlySales;
            this.monthlyReservations = monthlyReservations;
            this.imageFiles = files.stream().map(FileDTO::new).collect(Collectors.toList());

        }
    }
}
