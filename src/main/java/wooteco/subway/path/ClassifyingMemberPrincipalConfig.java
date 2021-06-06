package wooteco.subway.path;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.path.ui.ClassifyingMemberPrincipalArgumentResolver;

@Configuration
public class ClassifyingMemberPrincipalConfig implements WebMvcConfigurer {

    private final AuthService authService;

    public ClassifyingMemberPrincipalConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createClassifyingMemberPrincipalArgumentResolver());
    }

    @Bean
    public ClassifyingMemberPrincipalArgumentResolver createClassifyingMemberPrincipalArgumentResolver() {
        return new ClassifyingMemberPrincipalArgumentResolver(authService);
    }
}
