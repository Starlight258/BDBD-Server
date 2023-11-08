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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @PostMapping("/ready/{carwash_id}")
    public ResponseEntity<String> requestPaymentReady(
            @PathVariable("carwash_id") Long carwashId,
            @RequestBody PayRequest.PaymentReadyRequest paymentReadyRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return payService.requestPaymentReady(
                paymentReadyRequest.getRequestDto(),
                paymentReadyRequest.getSaveDTO(),
                carwashId,
                userDetails.getMember()
        );
    }

    @GetMapping("/redirect")
    public ResponseEntity<?> handlePaymentRedirect(@RequestParam String redirectUrl) {
        if (!redirectUrl.startsWith("https://online-pay.kakao.com/")) {
            throw new BadRequestError("Invalid redirect URL");
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.getForEntity(redirectUrl, String.class);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(redirectUrl));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            throw new InternalServerError("Error during redirect");
        }
    }


    @PostMapping("/approve/{carwash_id}/{bay_id}")
    public ResponseEntity<ReservationResponse.findLatestOneResponseDTO> requestPaymentApproval(
            @PathVariable("carwash_id") Long carwashId,
            @PathVariable("bay_id") Long bayId,
            @RequestBody PayRequest.PaymentApprovalRequestDTO requestDTO,
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