package bdbe.bdbd.pay;

import bdbe.bdbd._core.errors.exception.BadRequestError;
import bdbe.bdbd._core.errors.exception.InternalServerError;
import bdbe.bdbd._core.errors.security.CustomUserDetails;
import bdbe.bdbd.reservation.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @PostMapping("/user/payment/ready/{bay_id}")
    public ResponseEntity<String> requestPaymentReady(
            @PathVariable("bay_id") Long bayId,
            @RequestBody PayRequest.PaymentReadyRequest paymentReadyRequest
    ) {
        return payService.requestPaymentReady(
                paymentReadyRequest.getRequestDto(),
                paymentReadyRequest.getSaveDTO(),
                bayId
        );
    }


    @PostMapping("/user/payment/approve/{carwash_id}/{bay_id}")
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