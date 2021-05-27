package wooteco.subway.auth.infrastructure;

import org.springframework.http.HttpMethod;
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
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true; // preflight 설정
        }
        if (isGetMethod(request) && isNotMemberRequest(request)) {
            return true;
        }
        final String credentials = AuthorizationExtractor.extract(request);
        authService.validateToken(credentials);
        return true;
    }

    private boolean isGetMethod(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase(HttpMethod.GET.toString());
    }

    private boolean isNotMemberRequest(HttpServletRequest request) {
        return !request.getRequestURI().equalsIgnoreCase("/members/me");
    }
}
