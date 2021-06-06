package wooteco.subway.auth.infrastructure;

import io.swagger.models.HttpMethod;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = AuthorizationExtractor.extract(request);
        if (isRequestMethodIsPreflight(request)) {
            return true;
        }
        if (isRequestMethodIsGet(request) && isNotMemberLookUp(request)) {
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

    private boolean isNotMemberLookUp(HttpServletRequest request) {
        return !request.getRequestURI().equalsIgnoreCase("/api/members/me");
    }
}
