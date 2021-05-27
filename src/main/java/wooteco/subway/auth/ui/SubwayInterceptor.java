package wooteco.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.exception.InvalidTokenException;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;

public class SubwayInterceptor extends HandlerInterceptorAdapter {

    private final AuthService authService;

    public SubwayInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        String token = AuthorizationExtractor.extract(request);

        if (token == null) {
            throw new InvalidTokenException("토큰이 없습니다.");
        }

        if (authService.isNotValidToken(token)) {
            throw new InvalidTokenException("잘못된 토큰입니다.");
        }

        return super.preHandle(request, response, handler);
    }


}
