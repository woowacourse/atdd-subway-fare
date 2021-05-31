package wooteco.subway.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.ui.AnonymousPrincipalArgumentResolver;
import wooteco.subway.auth.ui.AuthenticationPrincipalArgumentResolver;
import wooteco.subway.auth.ui.LoginInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthService authService;

    public WebConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(authService);
    }

    @Bean
    public AnonymousPrincipalArgumentResolver createAnonymousPrincipalArgumentResolver() {
        return new AnonymousPrincipalArgumentResolver(authService);
    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor(authService);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(createAuthenticationPrincipalArgumentResolver());
        resolvers.add(createAnonymousPrincipalArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/members", "/login/token", "/paths");
    }
}
