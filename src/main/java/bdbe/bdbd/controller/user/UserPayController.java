package bdbe.bdbd.controller.user;

import bdbe.bdbd._core.security.CustomUserDetails;
import bdbe.bdbd.dto.pay.PayRequest;
import bdbe.bdbd.dto.reservation.ReservationResponse;
import bdbe.bdbd.service.pay.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
/**
 * 결제 관련 요청을 처리하는 사용자 API
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserPayController {

    private final PayService payService;

    @PostMapping("/payment/ready")
    public ResponseEntity<?> requestPaymentReady(
            @Valid @RequestBody PayRequest.PaymentReadyRequest paymentReadyRequest,
            Errors errors
    ) {
        Long bayId = paymentReadyRequest.getBayId();

        return payService.requestPaymentReady(
                paymentReadyRequest.getRequestDto(),
                paymentReadyRequest.getSaveDTO(),
                bayId
        );
    }


    @PostMapping("/payment/approve")
    public ResponseEntity<ReservationResponse.findLatestOneResponseDTO> requestPaymentApproval(
            @Valid @RequestBody PayRequest.PaymentApprovalRequestDTO requestDTO,
            Errors errors,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long carwashId = requestDTO.getCarwashId();
        Long bayId = requestDTO.getBayId();

        return payService.requestPaymentApproval(
                requestDTO.getPayApprovalRequestDTO(),
                carwashId,
                bayId,
                userDetails.getMember(),
                requestDTO.getSaveDTO()
        );
    }
}