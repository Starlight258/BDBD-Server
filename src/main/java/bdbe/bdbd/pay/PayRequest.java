package bdbe.bdbd.pay;

import bdbe.bdbd.reservation.ReservationRequest;
import bdbe.bdbd.reservation.ReservationRequest.SaveDTO;
import bdbe.bdbd.carwash.Carwash;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PayRequest {

    @Getter
    @Setter
    public static class PayReadyRequestDTO {

        @NotNull(message = "cid is requierd.")
        private String cid;

        @NotNull(message = "partner_order_id is required.")
        private String partner_order_id;

        @NotNull(message = "partner_user_id is required.")
        private String partner_user_id;

        @NotNull(message = "item_name is required.")
        private String item_name;

        @NotNull(message = "quantity is required.")
        private Integer quantity;

//        @NotNull(message = "total_amount is required.")
        private Integer total_amount;

//        @NotNull(message = "vat_amount is required.")
        private Integer vat_amount;

        @NotNull(message = "tax_free_amount is required.")
        private Integer tax_free_amount;

//        @NotNull(message = "approval_url is required.")
        private String approval_url;

//        @NotNull(message = "cancel_url is required.")
        private String cancel_url;

//        @NotNull(message = "fail_url is required.")
        private String fail_url;

    }

    @Getter
    @Setter
    public static class PayApprovalRequestDTO {

        @NotNull(message = "cid is requierd.")
        private String cid;

        @NotNull(message = "tid is requierd.")
        private String tid;

        @NotNull(message = "partner_order-id is requierd.")
        private String partner_order_id;

        @NotNull(message = "partner_user_id is requierd.")
        private String partner_user_id;

        @NotNull(message = "pg_token is requierd.")
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

        @NotNull(message = "carwashId is required.")
        private Long carwashId;

        @NotNull(message = "bayId id requied.")
        private Long bayId;
    }

}
