package wooteco.subway.auth.ui;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.exception.auth.LoginRequiredException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class LoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflightRequest(request)) {
            return true;
        }
        String credentials = AuthorizationExtractor.extract(request);
        if (Objects.isNull(credentials)) {
            return checkIfAnonymousPermissible(request);
        }
        authService.validate(credentials);
        return true;
    }

    private boolean isPreflightRequest(HttpServletRequest request) {
        return HttpMethod.OPTIONS.matches(request.getMethod());
    }

    private boolean checkIfAnonymousPermissible(HttpServletRequest request) {
        if ("/paths".equals(request.getRequestURI())) {
            return true;
        }
        throw new LoginRequiredException();
    }
}
