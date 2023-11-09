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


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/open")
public class OpenReservationController {

    private final ReservationService reservationService;

    // 세차장별 예약 내역 조회
    @GetMapping("/carwashes/{carwash_id}/bays")
    public ResponseEntity<?> findAllByCarwash(
            @PathVariable("carwash_id") Long carwashId
    )
    {
        ReservationResponse.findAllResponseDTO dto = reservationService.findAllByCarwash(carwashId);
        return ResponseEntity.ok(ApiUtils.success(dto));

    }
}