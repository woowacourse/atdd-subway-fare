package wooteco.subway.auth.ui;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = AuthorizationExtractor.extract(request);
        if (isRequestMethodIsPreflight(request)) {
            return false;
        }
        if (isRequestMethodIsGet(request)) {
            return true;
        }
        authService.validateToken(accessToken);
        return true;
    }

    private boolean isRequestMethodIsPreflight(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.name());
    }

    private boolean isRequestMethodIsGet(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.GET.name());
    }
}
