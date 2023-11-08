package bdbe.bdbd.reservation;

import bdbe.bdbd._core.errors.security.CustomUserDetails;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReservationRestController {

    private final ReservationService reservationService;

    @PostMapping("/user/carwashes/{bay_id}/payment")
    public ResponseEntity<?> findPayAmount(
            @PathVariable("bay_id") Long bayId,
            @Valid @RequestBody ReservationRequest.ReservationTimeDTO dto,
            Errors errors
    )
    {
        ReservationResponse.PayAmountDTO responseDTO = reservationService.findPayAmount(dto, bayId);

        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }

    // 예약 수정하기
    @PutMapping("/user/reservations/{reservation_id}")
    public ResponseEntity<?> updateReservation(
            @PathVariable("reservation_id") Long reservationId,
            @Valid @RequestBody ReservationRequest.UpdateDTO dto,
            Errors errors,
            @AuthenticationPrincipal CustomUserDetails userDetails

    )
    {
        reservationService.update(dto, reservationId, userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // 예약 취소하기
    @DeleteMapping("/user/reservations/{reservation_id}")
    public ResponseEntity<?> deleteReservation(
            @PathVariable("reservation_id") Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        reservationService.delete(reservationId, userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // 세차장별 예약 내역 조회
    @GetMapping("/public/carwashes/{carwash_id}/bays")
    public ResponseEntity<?> findAllByCarwash(
            @PathVariable("carwash_id") Long carwashId
    )
    {
        ReservationResponse.findAllResponseDTO dto = reservationService.findAllByCarwash(carwashId);
        return ResponseEntity.ok(ApiUtils.success(dto));

    }

    // 현재 시간 기준 예약 내역 조회
    @GetMapping("/user/reservations/current-status")
    public ResponseEntity<?> fetchCurrentStatusReservation(
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        ReservationResponse.fetchCurrentStatusReservationDTO dto = reservationService.fetchCurrentStatusReservation(userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(dto));
    }

    // 최근 예약 내역 가져오기
    @GetMapping("/user/reservations/recent")
    public ResponseEntity<?> updateReservation(
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        ReservationResponse.fetchRecentReservationDTO dto = reservationService.fetchRecentReservation(userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(dto));
    }

}