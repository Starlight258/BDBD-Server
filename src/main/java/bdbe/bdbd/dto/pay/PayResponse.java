package bdbe.bdbd.dto.pay;


import lombok.Getter;
import lombok.Setter;

public class PayResponse {
    // NOTE: 카카오페이 API 응답 명세에 따름
    @Getter
    @Setter
    public static class PayReadyResponseDTO {
        private String tid;
        private Boolean tms_result;
        private String next_redirect_app_url;
        private String next_redirect_mobile_url;
        private String next_redirect_pc_url;
        private String android_app_scheme;
        private String ios_app_scheme;
        private String created_at;
    }

    @Getter
    @Setter
    public static class PayApprovalResponseDTO {
        private String aid;
        private String tid;
        private String cid;
        private String partner_order_id;
        private String partner_user_id;
        private String payment_method_type;
        private String item_name;
        private Integer quantity;
        private Amount amount;
        private String created_at;
        private String approved_at;
    }

    @Getter
    @Setter
    public static class Amount {
        private Integer total;
        private Integer tax_free;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }

    @Getter
    @Setter
    public static class CardInfo {
        private String purchase_corp;
        private String purchase_corp_code;
        private String issuer_corp;
        private String issuer_corp_code;
        private String kakaopay_purchase_corp;
        private String kakaopay_purchase_corp_code;
        private String kakaopay_issuer_corp;
        private String kakaopay_issuer_corp_code;
        private String bin;
        private String card_type;
        private String install_month;
        private String approved_id;
        private String card_mid;
        private String interest_free_install;
        private String card_item_code;
    }
}