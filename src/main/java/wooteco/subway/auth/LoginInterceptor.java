package wooteco.subway.auth;

import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.exception.auth.LoginRequiredException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String credentials = AuthorizationExtractor.extract(request);
        if (credentials == null && request.getRequestURI().equals("/paths")) {
            return true;
        }
        if (credentials == null) {
            throw new LoginRequiredException();
        }
        authService.validate(credentials);
        return true;
    }
}
