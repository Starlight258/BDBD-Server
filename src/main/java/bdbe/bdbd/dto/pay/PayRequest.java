package bdbe.bdbd.dto.pay;

import bdbe.bdbd.dto.reservation.ReservationRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PayRequest {

    @Getter
    @Setter
    public static class PayReadyRequestDTO {
        // NOTE: 카카오페이 API 요청 명세에 따름
        @NotBlank(message = "Cid is required")
        private String cid;

        @NotBlank(message = "Partner_order_id is required")
        private String partner_order_id;

        @NotBlank(message = "Partner_user_id is required")
        private String partner_user_id;

        @NotBlank(message = "Item_name is required")
        private String item_name;

        @NotNull(message = "Quantity is required")
        private Integer quantity;

        @NotNull(message = "Total amount is required")
        private Integer total_amount;

        @NotNull(message = "Tax_free_amount is required")
        private Integer tax_free_amount;

    }

    @Getter
    @Setter
    public static class PayApprovalRequestDTO {

        @NotBlank(message = "Cid is required")
        private String cid;

        @NotBlank(message = "Tid is required")
        private String tid;

        @NotBlank(message = "Partner_order-id is required")
        private String partner_order_id;

        @NotBlank(message = "Partner_user_id is required")
        private String partner_user_id;

        @NotBlank(message = "Pg_token is required")
        private String pg_token;
    }

    @Getter
    @Setter
    public static class PaymentReadyRequest {
        @Valid
        private PayRequest.PayReadyRequestDTO requestDto;
        @Valid
        private ReservationRequest.SaveDTO saveDTO;

    }

    @Getter
    @Setter
    public static class PaymentApprovalRequestDTO {
        @Valid
        private PayRequest.PayApprovalRequestDTO payApprovalRequestDTO;
        @Valid
        private ReservationRequest.SaveDTO saveDTO;

    }
}
