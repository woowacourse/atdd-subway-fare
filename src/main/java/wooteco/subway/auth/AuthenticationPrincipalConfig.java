package wooteco.subway.auth;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.auth.infrastructure.UnAuthenticationPrincipalArgumentResolver;
import wooteco.subway.auth.ui.AuthenticationPrincipalArgumentResolver;
import wooteco.subway.auth.ui.LoginInterceptor;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    public AuthenticationPrincipalConfig(JwtTokenProvider jwtTokenProvider,
        AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
        argumentResolvers.add(createUnAuthenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(authService);
    }

    @Bean
    public UnAuthenticationPrincipalArgumentResolver createUnAuthenticationPrincipalArgumentResolver() {
        return new UnAuthenticationPrincipalArgumentResolver(authService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtTokenProvider))
            .addPathPatterns("/api/**")
            .excludePathPatterns("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**")
            .excludePathPatterns("/api/login/token")
            .excludePathPatterns("/api/members/**")
            .excludePathPatterns("/api/paths/**")
            .excludePathPatterns("/api/lines/map");
    }

}