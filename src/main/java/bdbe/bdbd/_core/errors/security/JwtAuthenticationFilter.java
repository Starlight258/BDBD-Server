package bdbe.bdbd._core.errors.security;

import bdbe.bdbd._core.errors.exception.UnAuthorizedError;
import bdbe.bdbd.member.Member;
import bdbe.bdbd.member.MemberRole;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final CacheService cacheService;  // CacheService 인스턴스를 저장할 필드 추가
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, CacheService cacheService) {
        super(authenticationManager);
        this.cacheService = cacheService;  // 생성자를 통해 CacheService 인스턴스 주입
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(JWTProvider.HEADER);
        String requestURI = request.getRequestURI();

        if ("/api/user/logout".equals(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        if (jwt == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            DecodedJWT decodedJWT = JWTProvider.verify(jwt);
            if (!cacheService.isTokenCached(jwt)) {
                throw new UnAuthorizedError("Token is not cached");
            }

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

        } catch (SignatureVerificationException sve) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid token signature");
            return;
        } catch (TokenExpiredException tee) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT has expired");
            return;
        } catch (UnAuthorizedError uae) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, uae.getMessage());  // UnAuthorizedError 예외 처리
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