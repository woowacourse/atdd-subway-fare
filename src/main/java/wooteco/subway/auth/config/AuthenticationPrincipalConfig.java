package wooteco.subway.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.auth.ui.AuthenticationPrincipalArgumentResolver;

import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalConfig(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(authService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtTokenProvider))
                .excludePathPatterns("/paths")
                .excludePathPatterns("/members")
                .excludePathPatterns("/login/token")
                .excludePathPatterns("/error")
                .addPathPatterns("/**");
    }
}
