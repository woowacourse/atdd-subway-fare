package wooteco.subway.auth.infrastructure;

import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.application.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isPreflight(request) || isGetRequestExcludeMemberRequest(request)) {
            return true;
        }
        String token = AuthorizationExtractor.extract(request);
        authService.validateToken(token);
        return true;
    }

    private boolean isGetRequestExcludeMemberRequest(HttpServletRequest request) {
        return "GET".equals(request.getMethod()) &&
                !request.getRequestURI().contains("/members/me");
    }

    private boolean isPreflight(HttpServletRequest request) {
        return "OPTIONS".equals(request.getMethod());
    }
}
