package wooteco.subway.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.auth.ui.AuthenticationPrincipalArgumentResolver;

import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    public AuthenticationPrincipalConfig(AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver) {
        this.authenticationPrincipalArgumentResolver = authenticationPrincipalArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(authenticationPrincipalArgumentResolver);
    }
}
