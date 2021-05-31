package wooteco.subway.auth.ui;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.exception.AuthorizationException;

public class LoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equals(request.getMethod()) || "GET".equals(request.getMethod())) {
            return true;
        }
        final String accessToken = AuthorizationExtractor.extract(request);
        if (Objects.isNull(accessToken)) {
            throw new AuthorizationException("로그인을 먼저 해야 합니다.");
        }
        authService.validateToken(accessToken);
        return true;
    }
}
