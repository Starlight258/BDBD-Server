package bdbe.bdbd.member;

import bdbe.bdbd._core.errors.exception.BadRequestError;
import bdbe.bdbd._core.errors.exception.UnAuthorizedError;
import bdbe.bdbd._core.errors.security.CacheService;
import bdbe.bdbd._core.errors.security.CustomUserDetails;
import bdbe.bdbd._core.errors.security.JWTProvider;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/owner")
public class OwnerRestController {
    private final OwnerService ownerService;
    @Autowired
    private CacheService cacheService;

    // (기능3) 이메일 중복체크
    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody @Valid MemberRequest.EmailCheckDTO emailCheckDTO, Errors errors) {
        ownerService.sameCheckEmail(emailCheckDTO.getEmail());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinOwner(@RequestBody @Valid MemberRequest.JoinDTO requestDTO, Errors errors) {
        ownerService.join(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid MemberRequest.LoginDTO requestDTO, Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
            throw new BadRequestError(errorMessage);
        }
        MemberResponse.LoginResponse response = ownerService.login(requestDTO);
        return ResponseEntity.ok().header(JWTProvider.HEADER, response.getJwtToken()).body(ApiUtils.success(null));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            MemberResponse.LogoutResponse response = cacheService.logout(token);
            if (response.isSuccess()) {
                return ResponseEntity.ok().body(ApiUtils.success("Logged out successfully"));
            } else {
                throw new UnAuthorizedError("Logout failed");
            }
        }
        throw new UnAuthorizedError("No token provided");
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

    @GetMapping("/carwashes")
    public ResponseEntity<?> fetchOwnerReservationOverview(
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        OwnerResponse.ReservationOverviewResponseDTO dto = ownerService.fetchOwnerReservationOverview(userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(dto));
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

    @GetMapping("/home")
    public ResponseEntity<?> fetchOwnerHomepage(
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        OwnerResponse.OwnerDashboardDTO dto = ownerService.fetchOwnerHomepage(userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(dto));
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



}

