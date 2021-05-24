package wooteco.subway.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.ui.AuthPrincipalArgumentResolver;
import wooteco.subway.auth.ui.LoginPrincipalArgumentResolver;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final AuthService authService;

    public AuthConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
        argumentResolvers.add(createLoginPrincipalArgumentResolver());
    }

    @Bean
    public AuthPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthPrincipalArgumentResolver(authService);
    }

    @Bean
    public LoginPrincipalArgumentResolver createLoginPrincipalArgumentResolver() {
        return new LoginPrincipalArgumentResolver(authService);
    }
}
