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
        if (isRequestPreflight(request)) {
            return true;
        }
        if (isNotPathsFind(request) && isRequestMethodGet(request) && isNotMemberLookUp(request)) {
            return true;
        }
        String credentials = AuthorizationExtractor.extract(request);
        if (isNotPathsFind(request)) {
            authService.validateToken(credentials);
        }
        request.setAttribute("credentials", credentials);
        return true;
    }

    private boolean isRequestPreflight(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.name());
    }

    private boolean isRequestMethodGet(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.GET.name());
    }

    private boolean isNotMemberLookUp(HttpServletRequest request) {
        return !request.getRequestURI().equalsIgnoreCase("/api/members/me");
    }

    private boolean isNotPathsFind(HttpServletRequest request) {
        return !request.getRequestURI().equalsIgnoreCase("/api/paths");
    }
}
