package wooteco.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.auth.util.JwtTokenProvider;
import wooteco.auth.web.api.AuthenticationPrincipalArgumentResolver;
import wooteco.auth.web.api.LoginInterceptor;

import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginInterceptor loginInterceptor;

    public AuthenticationPrincipalConfig(JwtTokenProvider jwtTokenProvider, LoginInterceptor loginInterceptor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(jwtTokenProvider);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/members/me");
    }
}
