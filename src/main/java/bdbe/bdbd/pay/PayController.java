package bdbe.bdbd.pay;

import bdbe.bdbd._core.errors.security.CustomUserDetails;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashService;
import bdbe.bdbd.reservation.ReservationRequest;
import bdbe.bdbd.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;
    private final ReservationService reservationService;
    private final CarwashService carwashService;

    @PostMapping("/ready")
    public ResponseEntity<String> requestPaymentReady(
            @RequestBody PayRequest.PaymentReadyRequest paymentReadyRequest) {
        return payService.requestPaymentReady(
                paymentReadyRequest.getRequestDto(),
                paymentReadyRequest.getSaveDTO(),
                Carwash.builder().build()
        );
    }

    @PostMapping("/approve/{carwash_id}/{bay_id}")
    public ResponseEntity<PayResponse.PaymentAndReservationResponseDTO> requestPaymentApproval(
            @RequestBody PayRequest.PayApprovalRequestDTO requestDto,
            @PathVariable("carwash_id") Long carwashId,
            @PathVariable("bay_id") Long bayId,
            @RequestBody ReservationRequest.SaveDTO saveDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return payService.requestPaymentApproval(requestDto, carwashId, bayId, userDetails.getMember(), saveDTO);
    }

}
