package wooteco.subway.auth.ui;

import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equals(request.getMethod()) || "GET".equals(request.getMethod())) {
            return true;
        }

        final String token = AuthorizationExtractor.extract(request);
        if (Objects.isNull(token) || !authService.validateToken(token)) {
            throw new AuthorizationException();
        }
        return true;
    }
}
