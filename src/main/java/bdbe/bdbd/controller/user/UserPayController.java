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

    @PostMapping("/payment/ready/{bay-id}")
    public ResponseEntity<?> requestPaymentReady(
            @PathVariable("bay-id") Long bayId,
            @Valid @RequestBody PayRequest.PaymentReadyRequest paymentReadyRequest,
            Errors errors
    ) {
        return payService.requestPaymentReady(
                paymentReadyRequest.getRequestDto(),
                paymentReadyRequest.getSaveDTO(),
                bayId
        );
    }


    @PostMapping("/payment/approve/{carwash-id}/{bay-id}")
    public ResponseEntity<ReservationResponse.findLatestOneResponseDTO> requestPaymentApproval(
            @PathVariable("carwash-id") Long carwashId,
            @PathVariable("bay-id") Long bayId,
            @Valid @RequestBody PayRequest.PaymentApprovalRequestDTO requestDTO,
            Errors errors,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return payService.requestPaymentApproval(
                requestDTO.getPayApprovalRequestDTO(),
                carwashId,
                bayId,
                userDetails.getMember(),
                requestDTO.getSaveDTO()
        );
    }
}