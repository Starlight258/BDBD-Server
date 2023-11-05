//package bdbe.bdbd._core.errors.security;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        // authException이 CustomAuthenticationException 타입인지 확인
//        if (authException.getCause() instanceof CustomAuthenticationException) {
//            CustomAuthenticationException customException = (CustomAuthenticationException) authException.getCause();
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, customException.getMessage()+ "yes");
//            System.out.println("commence 예외처리");
//        } else {
//            // 인증되지 않은 접근에 대한 처리
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Authentication token was either missing or invalid.");
//        }
//    }
//}