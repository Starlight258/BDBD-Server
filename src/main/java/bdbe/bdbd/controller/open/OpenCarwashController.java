package bdbe.bdbd.controller.open;

import bdbe.bdbd._core.exception.BadRequestError;
import bdbe.bdbd._core.utils.ApiUtils;
import bdbe.bdbd.dto.carwash.CarwashRequest;
import bdbe.bdbd.dto.carwash.CarwashResponse;
import bdbe.bdbd.service.carwash.CarwashService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/open")
public class OpenCarwashController {

    private final CarwashService carwashService;

    // 전체 세차장 목록 조회, 10개씩 페이징
    @GetMapping("/carwashes")
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        List<CarwashResponse.FindAllDTO> dtos = carwashService.findAll(page);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(dtos);
        return ResponseEntity.ok(apiResult);
    }


    @GetMapping("/carwashes/search")
    public ResponseEntity<?> findCarwashesByKeywords(@RequestParam List<Long> keywordIds,
                                                     @RequestParam double latitude,
                                                     @RequestParam double longitude) {
        // 위도 경도 유효성 검사
        validateLatitudeAndLongitude(latitude, longitude);

        CarwashRequest.SearchRequestDTO searchRequest = new CarwashRequest.SearchRequestDTO();
        searchRequest.setKeywordIds(keywordIds);

        searchRequest.setLatitude(latitude);
        searchRequest.setLongitude(longitude);

        List<CarwashRequest.CarwashDistanceDTO> carwashes = carwashService.findCarwashesByKeywords(searchRequest);
        return ResponseEntity.ok(ApiUtils.success(carwashes));
    }

    private void validateLatitudeAndLongitude(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90) {
            throw new BadRequestError(
                    BadRequestError.ErrorCode.VALIDATION_FAILED,
                    Collections.singletonMap("latitude", "Invalid latitude value : Latitude must be between -90 and 90.")
            );
        }
        if (longitude < -180 || longitude > 180) {
            throw new BadRequestError(
                    BadRequestError.ErrorCode.VALIDATION_FAILED,
                    Collections.singletonMap("longitude", "Invalid longitude value : Longitude must be between -180 and 180.")
            );
        }
    }


    @GetMapping("/carwashes/nearby")
    public ResponseEntity<?> findNearestCarwashesByUserLocation(@RequestParam double latitude, @RequestParam double longitude) {
        validateLatitudeAndLongitude(latitude, longitude);

        CarwashRequest.UserLocationDTO userLocation = new CarwashRequest.UserLocationDTO();
        userLocation.setLatitude(latitude);
        userLocation.setLongitude(longitude);
        List<CarwashRequest.CarwashDistanceDTO> carwashes = carwashService.findNearbyCarwashesByUserLocation(userLocation);
        return ResponseEntity.ok(ApiUtils.success(carwashes));
    }

    @GetMapping("/carwashes/recommended")
    public ResponseEntity<?> findNearestCarwash(@RequestParam double latitude, @RequestParam double longitude) {
        validateLatitudeAndLongitude(latitude, longitude);

        CarwashRequest.UserLocationDTO userLocation = new CarwashRequest.UserLocationDTO();
        userLocation.setLatitude(latitude);
        userLocation.setLongitude(longitude);
        List<CarwashRequest.CarwashDistanceDTO> carwashList = new ArrayList<>();
        CarwashRequest.CarwashDistanceDTO carwash = carwashService.findNearestCarwashByUserLocation(userLocation);
        if (carwash != null) {
            carwashList.add(carwash);
        }

        return ResponseEntity.ok(ApiUtils.success(carwashList));
    }

    @GetMapping("/carwashes/{carwash_id}/info")
    public ResponseEntity<?> findById(@PathVariable("carwash_id") Long carwashId) {
        CarwashResponse.findByIdDTO findByIdDTO = carwashService.getfindById(carwashId);
        return ResponseEntity.ok(ApiUtils.success(findByIdDTO));
    }

}




