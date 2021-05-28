package wooteco.subway.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.infrastructure.LoginInterceptor;
import wooteco.subway.auth.ui.AuthenticationPrincipalArgumentResolver;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {

    private final AuthService authService;

    public AuthenticationPrincipalConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final List<String> patterns = Arrays.asList("/members/me/**", "/lines/**", "/stations/**");
        registry.addInterceptor(new LoginInterceptor(authService))
                .addPathPatterns(patterns);
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(authService);
    }
}
