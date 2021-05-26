package wooteco.subway.path;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.auth.application.AuthService;

@Configuration
public class TempPrincipalConfig implements WebMvcConfigurer {
    private final AuthService authService;

    public TempPrincipalConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createTempConfigurationArgumentResolver());
    }

    @Bean
    public TempPrincipalConfig createTempConfigurationArgumentResolver() {
        return new TempPrincipalConfig(authService);
    }
}
