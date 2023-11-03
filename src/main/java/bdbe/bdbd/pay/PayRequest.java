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

    }

    @Getter
    @Setter
    public static class PayApprovalRequestDTO {
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
    @Getter
    @Setter
    public static class PaymentApprovalRequestDTO {
        private PayRequest.PayApprovalRequestDTO payApprovalRequestDTO;
        private ReservationRequest.SaveDTO saveDTO;
        private Long carwashId;
        private Long bayId;
    }

}
