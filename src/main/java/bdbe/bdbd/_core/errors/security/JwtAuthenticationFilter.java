package bdbe.bdbd._core.errors.security;

import bdbe.bdbd.member.Member;
import bdbe.bdbd.member.MemberRole;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(JWTProvider.HEADER);

        if (jwt == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            DecodedJWT decodedJWT = JWTProvider.verify(jwt);
            Long id = decodedJWT.getClaim("id").asLong();
            String role = decodedJWT.getClaim("role").asString();
            log.info("role: {}", role);

            MemberRole roleEnum = MemberRole.valueOf(role);
            Member member = Member.builder().id(id).role(roleEnum).build();

            CustomUserDetails myUserDetails = new CustomUserDetails(member);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            myUserDetails,
                            myUserDetails.getPassword(),
                            myUserDetails.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("디버그 : 인증 객체 만들어짐");
        } catch (SignatureVerificationException sve) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid token signature");
            return;
        } catch (TokenExpiredException tee) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT has expired");
            return;
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
            return;
        }
        chain.doFilter(request, response);
    }
    private void sendErrorResponse(HttpServletResponse response, int status, String message) {
        response.setStatus(status);
        try {
            response.getWriter().write(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}