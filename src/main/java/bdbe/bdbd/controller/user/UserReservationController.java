package bdbe.bdbd.controller.user;

import bdbe.bdbd._core.security.CustomUserDetails;
import bdbe.bdbd._core.utils.ApiUtils;
import bdbe.bdbd.dto.reservation.ReservationRequest;
import bdbe.bdbd.dto.reservation.ReservationResponse;
import bdbe.bdbd.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserReservationController {

    private final ReservationService reservationService;

    @PostMapping("/carwashes/{bay_id}/payment")
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
    @PutMapping("/reservations/{reservation_id}")
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
    @DeleteMapping("/reservations/{reservation_id}")
    public ResponseEntity<?> deleteReservation(
            @PathVariable("reservation_id") Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        reservationService.delete(reservationId, userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // 현재 시간 기준 예약 내역 조회
    @GetMapping("/reservations/current-status")
    public ResponseEntity<?> fetchCurrentStatusReservation(
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        ReservationResponse.fetchCurrentStatusReservationDTO dto = reservationService.fetchCurrentStatusReservation(userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(dto));
    }

    // 최근 예약 내역 가져오기
    @GetMapping("/reservations/recent")
    public ResponseEntity<?> updateReservation(
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        ReservationResponse.fetchRecentReservationDTO dto = reservationService.fetchRecentReservation(userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(dto));
    }

}