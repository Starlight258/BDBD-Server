package bdbe.bdbd.controller.user;

import bdbe.bdbd._core.security.CustomUserDetails;
import bdbe.bdbd.dto.pay.PayRequest;
import bdbe.bdbd.dto.reservation.ReservationRequest;
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
        ReservationRequest.SaveDTO saveDTO = paymentReadyRequest.getSaveDTO();
        return payService.requestPaymentReady(
                paymentReadyRequest.getRequestDto(),
                saveDTO
        );
    }


    @PostMapping("/payment/approve")
    public ResponseEntity<ReservationResponse.findLatestOneResponseDTO> requestPaymentApproval(
            @Valid @RequestBody PayRequest.PaymentApprovalRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long bayId = requestDTO.getSaveDTO().getBayId();

        return payService.requestPaymentApproval(
                requestDTO.getPayApprovalRequestDTO(),
                bayId,
                userDetails.getMember(),
                requestDTO.getSaveDTO()
        );
    }
}