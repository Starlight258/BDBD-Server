package bdbe.bdbd._core.security;

import bdbe.bdbd.model.Code.MemberRole;
import bdbe.bdbd.model.member.Member;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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

        try {
            if (jwt != null && !isNonProtectedUrl(request)) {
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
            }
        } catch (SignatureVerificationException sve) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid token signature");
            return;
        } catch (TokenExpiredException tee) {
            if (!isNonProtectedUrl(request)) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT has expired");
                return;
            }
            // If the URL is non-protected, ignore the expired token and continue
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isNonProtectedUrl(HttpServletRequest request) {
        AntPathRequestMatcher openMatcher = new AntPathRequestMatcher("/api/open/**");
        return openMatcher.matches(request);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) {
        response.setStatus(status);
        try {
            response.getWriter().write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
