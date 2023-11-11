package bdbe.bdbd.bay;

import bdbe.bdbd.repository.carwash.CarwashJPARepository;
import bdbe.bdbd.dto.bay.BayRequest;
import bdbe.bdbd.repository.bay.BayJPARepository;
import bdbe.bdbd.repository.location.LocationJPARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BayRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    LocationJPARepository locationJPARepository;

    @Autowired
    BayJPARepository bayJPARepository;

    @Autowired
    CarwashJPARepository carwashJPARepository;

    @Autowired
    private ObjectMapper om;

    @WithUserDetails("owner@nate.com")
    @Test
    @DisplayName("베이 추가")
    public void createBayTest() throws Exception{

        Long carwashId = carwashJPARepository.findFirstBy().getId();
        System.out.println(carwashId);

        BayRequest.SaveDTO saveDTO = new BayRequest.SaveDTO();
        saveDTO.setBayNum(9);

        String requestBody = om.writeValueAsString(saveDTO);

        ResultActions resultActions = mvc.perform(
                post("/api/owner/carwashes/{carwash-id}/bays", carwashId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @WithUserDetails("owner@nate.com")
    @Test
    @DisplayName("베이 활성화/비활성화")
    public void changeStatusTest() throws Exception {

        Long bayId = bayJPARepository.findFirstBy().getId();
        System.out.println("bayId = " + bayId);

        ResultActions resultActions = mvc.perform(
                put(String.format("/api/owner/bays/%d/status", bayId)) // String.format을 사용하여 URL 포맷팅
                        .param("status", "1")  // 쿼리 파라미터 추가
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 body:" + responseBody);

        resultActions.andExpect(jsonPath("$.success").value("true"));
    }



}
