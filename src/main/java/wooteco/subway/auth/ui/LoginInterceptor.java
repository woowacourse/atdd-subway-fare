package wooteco.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;

public class LoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if ("GET".equalsIgnoreCase(request.getMethod()) && !request.getRequestURI().contains("members")) {
            return true;
        }

        String credentials = AuthorizationExtractor.extract(request);
        authService.validateToken(credentials);
        return true;
    }
}
