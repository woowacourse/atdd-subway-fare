package wooteco.subway.auth.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.auth.ui.AgePrincipalArgumentResolver;
import wooteco.subway.auth.ui.AuthenticationPrincipalArgumentResolver;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalConfig(AuthService authService,
        JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtTokenProvider))
            .excludePathPatterns("/paths")
            .excludePathPatterns("/login/token")
            .excludePathPatterns("/members")
            .excludePathPatterns("/error")
            .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
        argumentResolvers.add(createArgumentAgeResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(authService);
    }

    @Bean
    public AgePrincipalArgumentResolver createArgumentAgeResolver() {
        return new AgePrincipalArgumentResolver(authService);
    }
}
