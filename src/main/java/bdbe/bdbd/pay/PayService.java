package bdbe.bdbd.pay;

import bdbe.bdbd._core.errors.exception.BadRequestError;
import bdbe.bdbd._core.errors.exception.ForbiddenError;
import bdbe.bdbd._core.errors.exception.NotFoundError;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.member.Member;
import bdbe.bdbd.reservation.Reservation;
import bdbe.bdbd.reservation.ReservationRequest;
import bdbe.bdbd.reservation.ReservationResponse;
import bdbe.bdbd.reservation.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class PayService {

    @Value("${kakao.admin.key}")
    private String adminKey;

    @Value("${payment.approval-url}")
    private String approval_url;

    @Value("${payment.cancel-url}")
    private String cancel_url;

    @Value("${payment.fail-url}")
    private String fail_url;


    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CarwashJPARepository carwashJpaRepository;

    public ResponseEntity<String> requestPaymentReady(PayRequest.PayReadyRequestDTO requestDto, ReservationRequest.SaveDTO saveDTO, Long carwashId, Member member) {

        Carwash carwash = carwashJpaRepository.findById(carwashId)
                .orElseThrow(() -> new BadRequestError("carwash id: " + carwashId + "not found"));

        int perPrice = carwash.getPrice();
        LocalDateTime startTime = saveDTO.getStartTime();
        LocalDateTime endTime = saveDTO.getEndTime();
        System.out.println(saveDTO.toString());

        int minutesDifference = (int) ChronoUnit.MINUTES.between(startTime, endTime);
        int blocksOf30Minutes = minutesDifference / 30;
        int price = perPrice * blocksOf30Minutes;

        int totalAmount = price;  //세금 포함 가격
//        int vatAmount = (int)(price * 0.1);  // 세금

        PayRequest.PayReadyRequestDTO dto = new PayRequest.PayReadyRequestDTO();
        dto.setTotal_amount(totalAmount);
//        dto.setVat_amount(vatAmount);

//        RestTemplate restTemplate = new RestTemplate();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + adminKey);

        PayRequest.PayReadyRequestDTO payReadyRequestDTO = new PayRequest.PayReadyRequestDTO();
        requestDto.setTotal_amount(totalAmount);
//        requestDto.setVat_amount(vatAmount);
        log.info("Request DTO: " + requestDto.toString());

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", requestDto.getCid());
        parameters.add("partner_order_id", requestDto.getPartner_order_id());
        parameters.add("partner_user_id", requestDto.getPartner_user_id());
        parameters.add("item_name", requestDto.getItem_name());
        parameters.add("quantity", requestDto.getQuantity().toString());
        parameters.add("total_amount", requestDto.getTotal_amount().toString());
//        parameters.add("vat_amount", requestDto.getVat_amount().toString());
        parameters.add("tax_free_amount", requestDto.getTax_free_amount().toString());
        parameters.add("approval_url", approval_url);
        parameters.add("cancel_url", cancel_url);
        parameters.add("fail_url", fail_url);
        log.info("Parameters: " + parameters.toString());


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
        String url = "https://kapi.kakao.com/v1/payment/ready";

        return restTemplate.postForEntity(url, request, String.class);
    }

    @Transactional
    public ResponseEntity<ReservationResponse.findLatestOneResponseDTO> requestPaymentApproval(
            PayRequest.PayApprovalRequestDTO requestDto,
            Long carwashId,
            Long bayId,
            Member member,
            ReservationRequest.SaveDTO saveDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + adminKey);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", requestDto.getCid());
        parameters.add("tid", requestDto.getTid());
        parameters.add("partner_order_id", requestDto.getPartner_order_id());
        parameters.add("partner_user_id", requestDto.getPartner_user_id());
        parameters.add("pg_token", requestDto.getPg_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
        String url = "https://kapi.kakao.com/v1/payment/approve";

        ResponseEntity<String> paymentApprovalResponse = restTemplate.postForEntity(url, request, String.class);

//        PayResponse.PaymentAndReservationResponseDTO responseDto = new PayResponse.PaymentAndReservationResponseDTO();
//        responseDto.setPaymentApprovalResponse(paymentApprovalResponse.getBody());

        Reservation reservation;

        if (paymentApprovalResponse.getStatusCode().is2xxSuccessful()) {
            reservation = reservationService.save(saveDTO, carwashId, bayId, member);  // 변수 이름 변경
        } else {
            log.error("Payment approval failed: " + paymentApprovalResponse.getBody());
            throw new BadRequestError("Payment approval failed");
        }
        ReservationResponse.findLatestOneResponseDTO responseDto = reservationService.fetchLatestReservation(reservation.getId());
        return ResponseEntity.ok(responseDto);

    }

}