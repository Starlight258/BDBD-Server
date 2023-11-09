package bdbe.bdbd.controller.owner;

import bdbe.bdbd._core.security.CustomUserDetails;
import bdbe.bdbd._core.utils.ApiUtils;
import bdbe.bdbd.dto.bay.BayRequest;
import bdbe.bdbd.service.bay.BayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/owner")
public class BayRestController {

    private final BayService bayService;

    @PostMapping("/carwashes/{carwash_id}/bays")
    public ResponseEntity<?> createBay(
            @PathVariable("carwash_id") Long carwashId,
            @Valid @RequestBody BayRequest.SaveDTO saveDTO,
            Errors errors,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        bayService.createBay(saveDTO, carwashId, userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PutMapping("/bays/{bay_id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable("bay_id") Long bayId,
            @RequestParam int status,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        bayService.changeStatus(bayId, status, userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(null));
    }


}