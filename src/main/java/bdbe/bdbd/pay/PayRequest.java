package bdbe.bdbd.pay;

import bdbe.bdbd.reservation.ReservationRequest;
import bdbe.bdbd.reservation.ReservationRequest.SaveDTO;
import bdbe.bdbd.carwash.Carwash;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PayRequest {

    @Getter
    @Setter
    public static class PayReadyRequestDTO {
        private String cid;
        private String partner_order_id;
        private String partner_user_id;
        private String item_name;
        private Integer quantity;
        private Integer total_amount;
        private Integer vat_amount;
        private Integer tax_free_amount;
        private String approval_url;
        private String cancel_url;
        private String fail_url;

        public static PayReadyRequestDTO fromSaveDTO(SaveDTO saveDTO, Carwash carwash) {
            int perPrice = carwash.getPrice();
            LocalDateTime startTime = saveDTO.getStartTime();
            LocalDateTime endTime = saveDTO.getEndTime();

            int minutesDifference = (int) ChronoUnit.MINUTES.between(startTime, endTime);
            int blocksOf30Minutes = minutesDifference / 30;
            int price = perPrice * blocksOf30Minutes;

            int totalAmount = (int)(price * 1.1);  //세금 포함 가격
            int vatAmount = (int)(price * 0.1);  // 세금

            PayReadyRequestDTO dto = new PayReadyRequestDTO();
            dto.setTotal_amount(totalAmount);
            dto.setVat_amount(vatAmount);
            return dto;
        }
    }

    @Getter
    @Setter
    public class PayApprovalRequestDTO {
        private String cid;
        private String tid;
        private String partner_order_id;
        private String partner_user_id;
        private String pg_token;
    }

    @Getter
    @Setter
    public static class PaymentReadyRequest {
        private PayRequest.PayReadyRequestDTO requestDto;
        private ReservationRequest.SaveDTO saveDTO;
    }

}
