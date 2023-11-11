package bdbe.bdbd.member.user;

import bdbe.bdbd._core.security.JWTProvider;
import bdbe.bdbd.dto.member.user.UserRequest;
import bdbe.bdbd.model.member.Member;
import bdbe.bdbd.repository.member.MemberJPARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    MemberJPARepository memberJPARepository;

    @BeforeEach
    public void setup() {
        UserRequest.JoinDTO mockUserDTO = new UserRequest.JoinDTO();
        mockUserDTO.setUsername("mockuser");
        mockUserDTO.setEmail("mock@naver.com");
        mockUserDTO.setPassword("asdf1234!");
        mockUserDTO.setTel("010-1234-5678");

        Member mockMember = mockUserDTO.toUserEntity(passwordEncoder.encode(mockUserDTO.getPassword()));

        memberJPARepository.save(mockMember);
    }


    @Autowired
    private ObjectMapper om;


    @Test
    public void checkTest() throws Exception {
        //given
        UserRequest.EmailCheckDTO requestDTO = new UserRequest.EmailCheckDTO();
        requestDTO.setEmail("bdbd@naver.com");
        String requestBody = om.writeValueAsString(requestDTO);
        //when
        ResultActions resultActions = mvc.perform(
                post("/api/open/member/check")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(jsonPath("$.success").value("true"))
                .andDo(print());
    }

    @Test
    public void joinTest() throws Exception {
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setUsername("imnewuser");
        requestDTO.setEmail("newuser@naver.com");
        requestDTO.setPassword("asdf1234!");
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/open/join/user")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("true"))
                .andDo(print());
    }

    @Test
    public void loginTest() throws Exception {
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail("mock@naver.com");
        requestDTO.setPassword("asdf1234!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/open/login/user")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(header().exists(JWTProvider.HEADER))
                .andExpect(jsonPath("$.success").value("true"))
                .andDo(print());
    }


    @Test
    public void sameEmailTest() throws Exception {

        String email = "mock@naver.com";
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setUsername("imnewuser");
        requestDTO.setEmail(email);
        requestDTO.setPassword("asdf1234!");
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/open/join/user")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void joinEmailExceptionTest() throws Exception {

        String email = "mocknaver.com";
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setUsername("imnewuser");
        requestDTO.setEmail(email);
        requestDTO.setPassword("asdf1234!");
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/open/join/user")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void joinPasswordExceptionTest() throws Exception {

        String email = "mock@naver.com";
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setUsername("imnewuser");
        requestDTO.setEmail(email);
        requestDTO.setPassword("asdf1234");
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/open/join/user")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void joinWrongEmailTest() throws Exception {
        String email = "aaaa@naver.com";
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail(email);
        requestDTO.setPassword("asdf1234!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/open/join/user")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void loginWrongPasswordTest() throws Exception {
        String email = "mock@naver.com";
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail(email);
        requestDTO.setPassword("aaaaaaaa!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/open/login/user")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void loginNotMatchPasswordTest() throws Exception {
        String email = "mock@naver.com";
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail(email);
        requestDTO.setPassword("aaaa1234!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/open/login/user")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.status").value(401))
                .andDo(print());
    }


}

