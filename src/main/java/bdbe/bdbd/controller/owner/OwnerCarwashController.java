package bdbe.bdbd.controller.owner;

import bdbe.bdbd._core.security.CustomUserDetails;
import bdbe.bdbd._core.utils.ApiUtils;
import bdbe.bdbd.dto.carwash.CarwashRequest;
import bdbe.bdbd.dto.carwash.CarwashResponse;
import bdbe.bdbd.dto.member.owner.OwnerResponse;
import bdbe.bdbd.service.carwash.CarwashService;
import bdbe.bdbd.service.file.FileService;
import bdbe.bdbd.service.member.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/owner")
public class OwnerCarwashController {

    private final CarwashService carwashService;

    private final FileService fileService;

    private final OwnerService ownerService;


    @PostMapping(value = "/carwashes/register")
    public ResponseEntity<?> save(@Valid @RequestPart("carwash") CarwashRequest.SaveDTO saveDTOs,
                                  Errors errors,
                                  @RequestPart("images") MultipartFile[] images,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        carwashService.save(saveDTOs, images, userDetails.getMember());

        return ResponseEntity.ok(ApiUtils.success(null));
    }


    @GetMapping("/carwashes/{carwash_id}/details") //세차장 정보 수정_세차장 기존 정보 불러오기
    public ResponseEntity<?> findCarwashByDetails(@PathVariable("carwash_id") Long carwashId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        CarwashResponse.carwashDetailsDTO carwashDetailsDTO = carwashService.findCarwashByDetails(carwashId, userDetails.getMember());

        return ResponseEntity.ok(ApiUtils.success(carwashDetailsDTO));
    }

    @DeleteMapping("/images/{image_id}")
    public ResponseEntity<?> deleteImage(
            @PathVariable("image_id") Long imageId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        fileService.deleteFile(imageId, userDetails.getMember());

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PutMapping("/carwashes/{carwash_id}/details")
    public ResponseEntity<?> updateCarwashDetails(
            @PathVariable("carwash_id") Long carwashId,
            @Valid @RequestPart("updateData") CarwashRequest.updateCarwashDetailsDTO updatedto,
            Errors errors,
            @RequestPart(value = "images", required = true) MultipartFile[] images,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CarwashResponse.updateCarwashDetailsResponseDTO updateCarwashDetailsDTO =
                carwashService.updateCarwashDetails(carwashId, updatedto, images, userDetails.getMember());

        return ResponseEntity.ok(ApiUtils.success(updateCarwashDetailsDTO));
    }

    @GetMapping("/carwashes")
    public ResponseEntity<?> fetchOwnerReservationOverview(
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        OwnerResponse.ReservationOverviewResponseDTO dto = ownerService.fetchOwnerReservationOverview(userDetails.getMember());

        return ResponseEntity.ok(ApiUtils.success(dto));
    }

    @GetMapping("/sales")
    public ResponseEntity<?> findAllOwnerReservation(
            @RequestParam(value = "carwash-id") List<Long> carwashIds,
            @RequestParam(value = "selected-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate selectedDate,
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        OwnerResponse.SaleResponseDTO saleResponseDTO = ownerService.findSales(carwashIds, selectedDate, userDetails.getMember());

        return ResponseEntity.ok(ApiUtils.success(saleResponseDTO));
    }

    @GetMapping("/carwashes/{carwash_id}")
    public ResponseEntity<?> fetchCarwashReservationOverview(
            @PathVariable("carwash_id") Long carwashId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        OwnerResponse.CarwashManageDTO dto = ownerService.fetchCarwashReservationOverview(carwashId, userDetails.getMember());

        return ResponseEntity.ok(ApiUtils.success(dto));
    }

    @GetMapping("/revenue")
    public ResponseEntity<?> findMonthRevenueByCarwash(
            @RequestParam(value = "carwash-id") List<Long> carwashIds,
            @RequestParam(value = "selected-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate selectedDate,
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        Map<String, Long> map = ownerService.findMonthRevenue(carwashIds, selectedDate, userDetails.getMember());

        return ResponseEntity.ok(ApiUtils.success(map));
    }

    @GetMapping("/reservation/{bay_id}")
    public ResponseEntity<?> fetchOwnerReservation(
            @PathVariable("bay_id") Long bayId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        OwnerResponse.ReservationCarwashListDTO dto = ownerService.findBayReservation(bayId, userDetails.getMember());

        return ResponseEntity.ok(ApiUtils.success(dto));
    }

    @GetMapping("/home")
    public ResponseEntity<?> fetchOwnerHomepage(
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        OwnerResponse.OwnerDashboardDTO dto = ownerService.fetchOwnerHomepage(userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(dto));
    }


}




