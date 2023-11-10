package bdbe.bdbd.controller.open;

import bdbe.bdbd._core.utils.ApiUtils;
import bdbe.bdbd.dto.reservation.ReservationResponse;
import bdbe.bdbd.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자가 특정 세차장의 예약 정보를 조회하는 공개 API
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/open")
public class OpenReservationController {

    private final ReservationService reservationService;

    @GetMapping("/carwashes/{carwash-id}/bays")
    public ResponseEntity<?> findAllByCarwash(
            @PathVariable("carwash-id") Long carwashId
    ) {
        ReservationResponse.findAllResponseDTO dto = reservationService.findAllByCarwash(carwashId);

        return ResponseEntity.ok(ApiUtils.success(dto));
    }
}