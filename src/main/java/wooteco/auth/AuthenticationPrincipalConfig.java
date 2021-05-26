package wooteco.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.auth.util.JwtTokenProvider;
import wooteco.auth.web.AuthenticationInterceptor;
import wooteco.auth.web.AuthenticationPrincipalArgumentResolver;

import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/login/token", "/api/members");
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(jwtTokenProvider);
    }

    @Bean
    public AuthenticationInterceptor authInterceptor() {
        return new AuthenticationInterceptor(jwtTokenProvider);
    }
}
