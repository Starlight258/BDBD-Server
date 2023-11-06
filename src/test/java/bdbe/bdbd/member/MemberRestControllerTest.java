package bdbe.bdbd.member;

import bdbe.bdbd._core.errors.security.JWTProvider;
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
public class MemberRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    MemberJPARepository memberJPARepository;

    @BeforeEach
    public void setup() {
        MemberRequest.JoinDTO mockUserDTO = new MemberRequest.JoinDTO();
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
        MemberRequest.EmailCheckDTO requestDTO = new MemberRequest.EmailCheckDTO();
        requestDTO.setEmail("bdbd@naver.com");
        String requestBody = om.writeValueAsString(requestDTO);
        //when
        ResultActions resultActions = mvc.perform(
                post("/api/user/check")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(jsonPath("$.success").value("true"))
                .andDo(print());
    }

    @Test
    public void joinTest() throws Exception {
        MemberRequest.JoinDTO requestDTO = new MemberRequest.JoinDTO();
        requestDTO.setUsername("imnewuser");
        requestDTO.setEmail("newuser@naver.com");
        requestDTO.setPassword("asdf1234!");
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/user/join")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("true"))
                .andDo(print());
    }

    @Test
    public void loginTest() throws Exception {
        MemberRequest.LoginDTO requestDTO = new MemberRequest.LoginDTO();
        requestDTO.setEmail("mock@naver.com");
        requestDTO.setPassword("asdf1234!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/user/login")
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
        MemberRequest.JoinDTO requestDTO = new MemberRequest.JoinDTO();
        requestDTO.setUsername("imnewuser");
        requestDTO.setEmail(email);
        requestDTO.setPassword("asdf1234!");
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/user/join")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.message").value("duplicate email exist : " + email))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void logoutTest() throws Exception {
        MemberRequest.LoginDTO loginRequestDTO = new MemberRequest.LoginDTO();
        loginRequestDTO.setEmail("mock@naver.com");
        loginRequestDTO.setPassword("asdf1234!");

        String loginRequestBody = om.writeValueAsString(loginRequestDTO);

        String jwtToken = mvc.perform(
                        post("/api/user/login")
                                .content(loginRequestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse()
                .getHeader(JWTProvider.HEADER);

        ResultActions logoutResultActions = mvc.perform(
                post("/api/user/logout")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logoutResultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.response").value("Logged out successfully"))
                .andDo(print());
    }


    @Test
    public void joinEmailExceptionTest() throws Exception {

        String email = "mocknaver.com";
        MemberRequest.JoinDTO requestDTO = new MemberRequest.JoinDTO();
        requestDTO.setUsername("imnewuser");
        requestDTO.setEmail(email);
        requestDTO.setPassword("asdf1234!");
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/user/join")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.message").value("이메일 형식으로 작성해주세요:email"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void joinPasswordExceptionTest() throws Exception {

        String email = "mock@naver.com";
        MemberRequest.JoinDTO requestDTO = new MemberRequest.JoinDTO();
        requestDTO.setUsername("imnewuser");
        requestDTO.setEmail(email);
        requestDTO.setPassword("asdf1234");
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/user/join")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.message").value("영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.:password"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void loginWrongEmailTest() throws Exception {
        String email = "aaaa@naver.com";
        MemberRequest.LoginDTO requestDTO = new MemberRequest.LoginDTO();
        requestDTO.setEmail(email);
        requestDTO.setPassword("asdf1234!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/user/login")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.message").value("email not found : "+requestDTO.getEmail()))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void loginWrongPasswordTest() throws Exception {
        String email = "mock@naver.com";
        MemberRequest.LoginDTO requestDTO = new MemberRequest.LoginDTO();
        requestDTO.setEmail(email);
        requestDTO.setPassword("aaaaaaaa!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/user/login")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.message").value("영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.:password"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void loginNotMatchPasswordTest() throws Exception {
        String email = "mock@naver.com";
        MemberRequest.LoginDTO requestDTO = new MemberRequest.LoginDTO();
        requestDTO.setEmail(email);
        requestDTO.setPassword("aaaa1234!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/user/login")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.message").value("wrong password"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }


}

