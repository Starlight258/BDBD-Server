package bdbe.bdbd._core.errors.security;


import bdbe.bdbd._core.errors.exception.ForbiddenError;
import bdbe.bdbd._core.errors.exception.UnAuthorizedError;
import bdbe.bdbd._core.errors.utils.FilterResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class SecurityConfig {


    @Autowired
    private CacheService cacheService;  // CacheService를 주입받습니다.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {

        private final CacheService cacheService;  // CacheService 인스턴스를 저장할 필드 추가

        public CustomSecurityFilterManager(CacheService cacheService) {
            this.cacheService = cacheService;
        }

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager, cacheService));  // CacheService 인스턴스 제공
            super.configure(builder);
        }
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 해제
        http.csrf().disable(); // postman 접근해야 함!! - CSR 할때!!

        // iframe 거부
        http.headers().frameOptions().sameOrigin();

        // cors 재설정
        http.cors().configurationSource(configurationSource());

        // jSessionId 사용 거부 (5번을 설정하면 jsessionId가 거부되기 때문에 4번은 사실 필요 없다)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // form 로긴 해제 (UsernamePasswordAuthenticationFilter 비활성화)
        http.formLogin().disable();

        // 로그인 인증창이 뜨지 않게 비활성화
        http.httpBasic().disable();

        // 커스텀 필터 적용 (시큐리티 필터 교환)
        http.apply(new CustomSecurityFilterManager(cacheService));  // CacheService 인스턴스를 제공합니다.
        // 인증 실패 처리
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            FilterResponseUtils.unAuthorized(response, new UnAuthorizedError("Not authenticated"));
        });

        // 권한 실패 처리
        http.exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) -> {
            FilterResponseUtils.forbidden(response, new ForbiddenError("Permission denied"));
        });

        // 인증, 권한 필터 설정: 오너와 사용자 어드민 세가지로 url 접근 수정
        http.authorizeRequests(authorize -> authorize
                .antMatchers("/api/owner/join", "/api/owner/login").permitAll()
                .antMatchers("/api/user/join", "/api/user/login").permitAll()
                .antMatchers("/api/admin/join", "/api/admin/login").permitAll()
                .antMatchers("/api/user/check", "/api/user/check").permitAll()
                .antMatchers("/api/owner/check", "/api/owner/check").permitAll()
                .antMatchers("/api/admin/**").access("hasRole('ADMIN')")
                .antMatchers("/api/owner/**").access("hasRole('OWNER')")
                .anyRequest().permitAll());
        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE, HEAD (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트엔드 IP만 허용 react)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization"); // 권고사항
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
