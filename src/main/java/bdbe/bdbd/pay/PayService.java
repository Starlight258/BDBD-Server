package bdbe.bdbd.pay;

import bdbe.bdbd._core.errors.exception.BadRequestError;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.member.Member;
import bdbe.bdbd.reservation.Reservation;
import bdbe.bdbd.reservation.ReservationRequest;
import bdbe.bdbd.reservation.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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


    public ResponseEntity<String> requestPaymentReady(PayRequest.PayReadyRequestDTO requestDto, ReservationRequest.SaveDTO saveDTO, Carwash carwash) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + adminKey);

        PayRequest.PayReadyRequestDTO payReadyRequestDTO = PayRequest.PayReadyRequestDTO.fromSaveDTO(saveDTO, carwash);
        requestDto.setTotal_amount(payReadyRequestDTO.getTotal_amount());
        requestDto.setVat_amount(payReadyRequestDTO.getVat_amount());
        log.info("Request DTO: " + requestDto.toString());


        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", requestDto.getCid());
        parameters.add("partner_order_id", requestDto.getPartner_order_id());
        parameters.add("partner_user_id", requestDto.getPartner_user_id());
        parameters.add("item_name", requestDto.getItem_name());
        parameters.add("quantity", requestDto.getQuantity().toString());
        parameters.add("total_amount", requestDto.getTotal_amount().toString());
        parameters.add("vat_amount", requestDto.getVat_amount().toString());
        parameters.add("tax_free_amount", requestDto.getTax_free_amount().toString());
        parameters.add("approval_url", approval_url);
        parameters.add("cancel_url", cancel_url);
        parameters.add("fail_url", fail_url);
        log.info("Parameters: " + parameters.toString());


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
        String url = "https://kapi.kakao.com/v1/payment/ready";

        return restTemplate.postForEntity(url, request, String.class);
    }

    public ResponseEntity<PayResponse.PaymentAndReservationResponseDTO> requestPaymentApproval(
            PayRequest.PayApprovalRequestDTO requestDto,
            Long carwashId,
            Long bayId,
            Member sessionMember,
            ReservationRequest.SaveDTO saveDTO) {

        RestTemplate restTemplate = new RestTemplate();

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

        PayResponse.PaymentAndReservationResponseDTO responseDto = new PayResponse.PaymentAndReservationResponseDTO();
        responseDto.setPaymentApprovalResponse(paymentApprovalResponse.getBody());

        if (paymentApprovalResponse.getStatusCode().is2xxSuccessful()) {
            Reservation reservation = reservationService.save(saveDTO, carwashId, bayId, sessionMember);
            responseDto.setReservation(reservation);
        } else {
            log.error("Payment approval failed: " + paymentApprovalResponse.getBody());
            throw new BadRequestError("Payment approval failed");

        }
        return ResponseEntity.ok(responseDto);
    }
}