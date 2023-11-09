package bdbe.bdbd.controller.user;

import bdbe.bdbd._core.security.CustomUserDetails;
import bdbe.bdbd.dto.pay.PayRequest;
import bdbe.bdbd.service.pay.PayService;
import bdbe.bdbd.dto.reservation.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @PostMapping("/payment/ready/{bay_id}")
    public ResponseEntity<?> requestPaymentReady(
            @PathVariable("bay_id") Long bayId,
            @RequestBody @Valid  PayRequest.PaymentReadyRequest paymentReadyRequest,
            Errors errors
    ) {
        System.out.println("error");
        return payService.requestPaymentReady(
                paymentReadyRequest.getRequestDto(),
                paymentReadyRequest.getSaveDTO(),
                bayId
        );
    }


    @PostMapping("/payment/approve/{carwash_id}/{bay_id}")
    public ResponseEntity<ReservationResponse.findLatestOneResponseDTO> requestPaymentApproval(
            @PathVariable("carwash_id") Long carwashId,
            @PathVariable("bay_id") Long bayId,
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