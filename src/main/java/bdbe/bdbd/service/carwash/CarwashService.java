package bdbe.bdbd.service.carwash;


import bdbe.bdbd._core.exception.BadRequestError;
import bdbe.bdbd._core.exception.ForbiddenError;
import bdbe.bdbd._core.exception.NotFoundError;
import bdbe.bdbd._core.utils.Haversine;
import bdbe.bdbd.model.Code.DayType;
import bdbe.bdbd.repository.carwash.CarwashJPARepository;
import bdbe.bdbd.model.carwash.Carwash;
import bdbe.bdbd.repository.bay.BayJPARepository;
import bdbe.bdbd.dto.carwash.CarwashResponse;
import bdbe.bdbd.dto.carwash.CarwashResponse.updateCarwashDetailsResponseDTO;
import bdbe.bdbd.dto.carwash.CarwashRequest;
import bdbe.bdbd.model.file.File;
import bdbe.bdbd.repository.file.FileJPARepository;
import bdbe.bdbd.service.file.S3ProxyUploadService;
import bdbe.bdbd.model.keyword.Keyword;
import bdbe.bdbd.repository.keyword.KeywordJPARepository;
import bdbe.bdbd.model.keyword.carwashKeyword.CarwashKeyword;
import bdbe.bdbd.repository.keyword.carwashKeyword.CarwashKeywordJPARepository;
import bdbe.bdbd.model.location.Location;
import bdbe.bdbd.repository.location.LocationJPARepository;
import bdbe.bdbd.model.member.Member;
import bdbe.bdbd.model.optime.Optime;
import bdbe.bdbd.repository.optime.OptimeJPARepository;
import bdbe.bdbd.dto.reservation.ReservationResponse;
import bdbe.bdbd.repository.review.ReviewJPARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class CarwashService {
    private final CarwashJPARepository carwashJPARepository;
    private final KeywordJPARepository keywordJPARepository;
    private final LocationJPARepository locationJPARepository;
    private final OptimeJPARepository optimeJPARepository;
    private final CarwashKeywordJPARepository carwashKeywordJPARepository;
    private final ReviewJPARepository reviewJPARepository;
    private final BayJPARepository bayJPARepository;
    private final FileJPARepository fileJPARepository;
    private final S3ProxyUploadService s3ProxyUploadService;

    public List<CarwashResponse.FindAllDTO> findAll(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("Invalid page number.");
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<Carwash> carwashEntities = carwashJPARepository.findAll(pageable);

        if (carwashEntities == null || !carwashEntities.hasContent()) {
            throw new NoSuchElementException("No carwash entities found.");
        }

        List<CarwashResponse.FindAllDTO> carwashResponses = carwashEntities.getContent().stream()
                .map(CarwashResponse.FindAllDTO::new)
                .collect(Collectors.toList());

        if (carwashResponses == null || carwashResponses.isEmpty()) {
            throw new NoSuchElementException("No carwash entities transformed.");
        }

        return carwashResponses;
    }

    @Transactional
    public void save(CarwashRequest.SaveDTO saveDTO, MultipartFile[] images, Member sessionMember) {
        Location location = saveDTO.toLocationEntity();
        locationJPARepository.save(location);

        Carwash carwash = saveDTO.toCarwashEntity(location, sessionMember);
        carwashJPARepository.save(carwash);

        List<Optime> optimes = saveDTO.toOptimeEntities(carwash);
        optimeJPARepository.saveAll(optimes);

        List<Long> keywordIdList = saveDTO.getKeywordId();
        List<CarwashKeyword> carwashKeywordList = new ArrayList<>();
        for (Long keywordId : keywordIdList) {
            Keyword keyword = keywordJPARepository.findById(keywordId)
                    .orElseThrow(() -> new IllegalArgumentException("Keyword not found"));

            CarwashKeyword carwashKeyword = CarwashKeyword.builder().carwash(carwash).keyword(keyword).build();
            carwashKeywordList.add(carwashKeyword);
        }
        carwashKeywordJPARepository.saveAll(carwashKeywordList);

        if (images != null && images.length > 0) {
            uploadAndSaveFiles(images, carwash);
        }

    }

    @Transactional
    public List<ReservationResponse.ImageDTO> uploadAndSaveFiles(MultipartFile[] images, Carwash carwash) {
        List<File> existingFiles = fileJPARepository.findByCarwash_IdAndIsDeletedFalse(carwash.getId());
        for (File file : existingFiles) {
            file.changeDeletedFlag(true);
        }

        fileJPARepository.saveAll(existingFiles);

        List<ReservationResponse.ImageDTO> updatedImages = new ArrayList<>();
        try {
            List<String> imageUrls = uploadFiles(Arrays.asList(images));

            List<File> savedFiles = saveFileEntities(imageUrls, carwash);

            for (File file : savedFiles) {
                updatedImages.add(new ReservationResponse.ImageDTO(file));
            }

        } catch (Exception e) {
            logger.error("File upload and save failed: " + e.getMessage(), e);
            throw new RuntimeException("File upload and save failed", e);
        }
        return updatedImages;
    }


    private List<String> uploadFiles(List<MultipartFile> files) {
        try {
            logger.info("Image upload start");
            return s3ProxyUploadService.uploadFiles(files);
        } catch (Exception e) {
            logger.error("File upload failed: " + e.getMessage(), e);
            throw new RuntimeException("File upload failed", e);
        }
    }

    private List<File> saveFileEntities(List<String> imageUrls, Carwash carwash) {
        List<File> files = new ArrayList<>();
        try {
            for (String imageUrl : imageUrls) {
                File newFile = File.builder()
                        .name(imageUrl.substring(imageUrl.lastIndexOf("/") + 1))
                        .url(imageUrl)
                        .uploadedAt(LocalDateTime.now())
                        .carwash(carwash)
                        .build();
                files.add(newFile);
            }
            files = fileJPARepository.saveAll(files);
            logger.info("File entities saved successfully");
        } catch (Exception e) {
            logger.error("Saving file entities failed: " + e.getMessage(), e);
            throw new RuntimeException("Saving file entities failed", e);
        }
        return files;
    }

    public List<CarwashRequest.CarwashDistanceDTO> findNearbyCarwashesByUserLocation(CarwashRequest.UserLocationDTO userLocation) {
        List<Carwash> carwashes = carwashJPARepository.findCarwashesWithin10Kilometers(userLocation.getLatitude(), userLocation.getLongitude());

        return carwashes.stream()
                .map(carwash -> {
                    double distance = Haversine.distance(userLocation.getLatitude(), userLocation.getLongitude(),
                            carwash.getLocation().getLatitude(), carwash.getLocation().getLongitude());
                    double rate = carwash.getRate();
                    int price = carwash.getPrice();

                    File file = fileJPARepository.findFirstByCarwashIdAndIsDeletedFalseOrderByUploadedAtAsc(carwash.getId()).orElse(null);
                    CarwashRequest.CarwashDistanceDTO dto = new CarwashRequest.CarwashDistanceDTO(carwash.getId(), carwash.getName(), carwash.getLocation(), distance, rate, price, file);

                    return dto;
                })
                .sorted(Comparator.comparingDouble(CarwashRequest.CarwashDistanceDTO::getDistance))
                .collect(Collectors.toList());
    }

    public CarwashRequest.CarwashDistanceDTO findNearestCarwashByUserLocation(CarwashRequest.UserLocationDTO userLocation) {
        List<Carwash> carwashes = carwashJPARepository.findCarwashesWithin10Kilometers(userLocation.getLatitude(), userLocation.getLongitude());

        return carwashes.stream()
                .map(carwash -> {
                    double distance = Haversine.distance(userLocation.getLatitude(), userLocation.getLongitude(),
                            carwash.getLocation().getLatitude(), carwash.getLocation().getLongitude());
                    double rate = carwash.getRate();
                    int price = carwash.getPrice();

                    File file = fileJPARepository.findFirstByCarwashIdAndIsDeletedFalseOrderByUploadedAtAsc(carwash.getId()).orElse(null);
                    CarwashRequest.CarwashDistanceDTO dto = new CarwashRequest.CarwashDistanceDTO(carwash.getId(), carwash.getName(), carwash.getLocation(), distance, rate, price, file);

                    return dto;
                })
                .min(Comparator.comparingDouble(CarwashRequest.CarwashDistanceDTO::getDistance))
                .orElse(null);
    }

    public List<CarwashRequest.CarwashDistanceDTO> findCarwashesByKeywords(CarwashRequest.SearchRequestDTO searchRequest) {
        List<Carwash> carwashesWithin10Km = carwashJPARepository.findCarwashesWithin10Kilometers(searchRequest.getLatitude(), searchRequest.getLongitude());

        List<Keyword> selectedKeywords = keywordJPARepository.findAllById(searchRequest.getKeywordIds());
        if (searchRequest.getKeywordIds().size() != selectedKeywords.size()) {
            throw new IllegalArgumentException("Some of the keyword IDs you provided are invalid ");
        }

        List<CarwashKeyword> carwashKeywords = carwashKeywordJPARepository.findByKeywordIn(selectedKeywords);

        Set<Long> carwashIdsWithSelectedKeywords = carwashKeywords.stream()
                .map(carwashKeyword -> carwashKeyword.getCarwash().getId())
                .collect(Collectors.toSet());

        List<CarwashRequest.CarwashDistanceDTO> result = carwashesWithin10Km.stream()
                .filter(carwash -> carwashIdsWithSelectedKeywords.contains(carwash.getId()))
                .map(carwash -> {
                    double distance = Haversine.distance(
                            searchRequest.getLatitude(), searchRequest.getLongitude(),
                            carwash.getLocation().getLatitude(), carwash.getLocation().getLongitude()
                    );
                    double rate = carwash.getRate();
                    int price = carwash.getPrice();

                    File file = fileJPARepository.findFirstByCarwashIdAndIsDeletedFalseOrderByUploadedAtAsc(carwash.getId()).orElse(null);
                    CarwashRequest.CarwashDistanceDTO dto = new CarwashRequest.CarwashDistanceDTO(carwash.getId(), carwash.getName(), carwash.getLocation(), distance, rate, price, file);

                    return dto;
                })
                .sorted(Comparator.comparingDouble(CarwashRequest.CarwashDistanceDTO::getDistance))
                .collect(Collectors.toList());

        return result;
    }

    public CarwashResponse.findByIdDTO getfindById(Long carwashId) {

        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new IllegalArgumentException("not found carwash"));
        int reviewCnt = reviewJPARepository.findByCarwash_Id(carwashId).size();
        int bayCnt = bayJPARepository.findByCarwashId(carwashId).size();
        Location location = locationJPARepository.findById(carwash.getLocation().getId())
                .orElseThrow(() -> new NoSuchElementException("location not found"));
        List<Long> keywordIds = carwashKeywordJPARepository.findKeywordIdsByCarwashId(carwashId);

        List<Optime> optimeList = optimeJPARepository.findByCarwash_Id(carwashId);
        Map<DayType, Optime> optimeByDayType = new EnumMap<>(DayType.class);
        optimeList.forEach(ol -> optimeByDayType.put(ol.getDayType(), ol));

        Optime weekOptime = optimeByDayType.get(DayType.WEEKDAY);
        Optime endOptime = optimeByDayType.get(DayType.WEEKEND);

        List<File> imageFiles = fileJPARepository.findByCarwash_IdAndIsDeletedFalse(carwashId);

        return new CarwashResponse.findByIdDTO(carwash, reviewCnt, bayCnt, location, keywordIds, weekOptime, endOptime, imageFiles);
    }

    public CarwashResponse.carwashDetailsDTO findCarwashByDetails(Long carwashId, Member member) {

        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new IllegalArgumentException("carwash not found"));
        if (carwash.getMember().getId() != member.getId())
            throw new ForbiddenError(
                    ForbiddenError.ErrorCode.RESOURCE_ACCESS_FORBIDDEN,
                    Collections.singletonMap("MemberId", "Member is not the owner of the carwash.")
            );

        Location location = locationJPARepository.findById(carwash.getLocation().getId())
                .orElseThrow(() -> new NoSuchElementException("location not found"));
        List<Long> keywordIds = carwashKeywordJPARepository.findKeywordIdsByCarwashId(carwashId);

        List<Optime> optimeList = optimeJPARepository.findByCarwash_Id(carwashId);
        Map<DayType, Optime> optimeByDayType = new EnumMap<>(DayType.class);
        optimeList.forEach(ol -> optimeByDayType.put(ol.getDayType(), ol));

        Optime weekOptime = optimeByDayType.get(DayType.WEEKDAY);
        Optime endOptime = optimeByDayType.get(DayType.WEEKEND);

        List<File> imageFiles = fileJPARepository.findByCarwash_IdAndIsDeletedFalse(carwashId);

        return new CarwashResponse.carwashDetailsDTO(carwash, location, keywordIds, weekOptime, endOptime,imageFiles);

    }
    private static final Logger logger = LoggerFactory.getLogger(CarwashService.class);

    @Transactional
    public CarwashResponse.updateCarwashDetailsResponseDTO updateCarwashDetails(Long carwashId, CarwashRequest.updateCarwashDetailsDTO updatedto, MultipartFile[] images, Member member) {
        updateCarwashDetailsResponseDTO response = new updateCarwashDetailsResponseDTO();
        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("CarwashId", "Carwash not found")
                ));
        if (carwash.getMember().getId() != member.getId()) {
            throw new ForbiddenError(
                    ForbiddenError.ErrorCode.RESOURCE_ACCESS_FORBIDDEN,
                    Collections.singletonMap("MemberId", "Member is not the owner of the carwash.")
            );
        }
        carwash.setName(updatedto.getName());
        carwash.setTel(updatedto.getTel());
        carwash.setDes(updatedto.getDescription());
        carwash.setPrice(updatedto.getPrice());
        response.updateCarwashPart(carwash);

        CarwashRequest.updateLocationDTO updateLocationDTO = updatedto.getLocationDTO();
        Location location = locationJPARepository.findById(carwash.getLocation().getId())
                .orElseThrow(() -> new NotFoundError(
                        NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                        Collections.singletonMap("LocationId", "Location not found")
                ));


        location.updateAddress(updateLocationDTO.getAddress(), updateLocationDTO.getPlaceName()
                , updateLocationDTO.getLatitude(), updateLocationDTO.getLongitude());
        response.updateLocationPart(location);

        CarwashRequest.updateOperatingTimeDTO updateOperatingTimeDTO = updatedto.getOptime();

        List<Optime> optimeList = optimeJPARepository.findByCarwash_Id(carwashId);
        Map<DayType, Optime> optimeByDayType = new EnumMap<>(DayType.class);
        optimeList.forEach(ol -> optimeByDayType.put(ol.getDayType(), ol));

        Optime weekOptime = optimeByDayType.get(DayType.WEEKDAY);
        Optime endOptime = optimeByDayType.get(DayType.WEEKEND);

        weekOptime.setStartTime(updateOperatingTimeDTO.getWeekday().getStart());
        weekOptime.setEndTime(updateOperatingTimeDTO.getWeekday().getEnd());
        endOptime.setStartTime(updateOperatingTimeDTO.getWeekend().getStart());
        endOptime.setEndTime(updateOperatingTimeDTO.getWeekend().getEnd());

        response.updateOptimePart(weekOptime, endOptime);

        List<Long> newKeywordIds = updatedto.getKeywordId();

        List<Long> existingKeywordIds = carwashKeywordJPARepository.findKeywordIdsByCarwashId(carwashId);

        List<Long> keywordsToDelete = existingKeywordIds.stream()
                .filter(id -> !newKeywordIds.contains(id))
                .collect(Collectors.toList());
        carwashKeywordJPARepository.deleteByCarwashIdAndKeywordIds(carwashId, keywordsToDelete);

        List<Long> keywordsToAdd = newKeywordIds.stream()
                .filter(id -> !existingKeywordIds.contains(id))
                .collect(Collectors.toList());

        List<Keyword> keywordList = keywordJPARepository.findAllById(keywordsToAdd);
        if (keywordList.size() != keywordsToAdd.size()) {
            throw new NotFoundError(
                    NotFoundError.ErrorCode.RESOURCE_NOT_FOUND,
                    Collections.singletonMap("KeywordId", "Keyword not found")
            );
        }

        List<CarwashKeyword> newCarwashKeywords = new ArrayList<>();
        for (Keyword keyword : keywordList) {
            CarwashKeyword carwashKeyword = CarwashKeyword.builder()
                    .carwash(carwash)
                    .keyword(keyword)
                    .build();
            newCarwashKeywords.add(carwashKeyword);
        }
        carwashKeywordJPARepository.saveAll(newCarwashKeywords);

        List<Long> updateKeywordIds = carwashKeywordJPARepository.findKeywordIdsByCarwashId(carwashId);
        response.updateKeywordPart(updateKeywordIds);

        if (images != null && images.length > 0) {
            List<ReservationResponse.ImageDTO> updatedImages = uploadAndSaveFiles(images, carwash);
            response.setImages(updatedImages);
        }
        return response;
    }

}
