package wooteco.subway.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.auth.ui.AuthenticMemberTypeArgumentResolver;
import wooteco.subway.auth.ui.AuthenticationPrincipalArgumentResolver;

import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;
    private AuthenticMemberTypeArgumentResolver authenticMemberTypeArgumentResolver;

    public AuthenticationPrincipalConfig(AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver,
                                         AuthenticMemberTypeArgumentResolver authenticMemberTypeArgumentResolver) {
        this.authenticationPrincipalArgumentResolver = authenticationPrincipalArgumentResolver;
        this.authenticMemberTypeArgumentResolver = authenticMemberTypeArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(authenticationPrincipalArgumentResolver);
        argumentResolvers.add(authenticMemberTypeArgumentResolver);
    }
}
