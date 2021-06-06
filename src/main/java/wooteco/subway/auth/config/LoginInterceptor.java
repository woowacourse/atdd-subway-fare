package wooteco.subway.auth.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.exception.AuthExceptionSet;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.SubwayException;

public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null) {
            throw new SubwayException(AuthExceptionSet.NOT_EXIST_TOKEN_EXCEPTION);
        }

        String credentials = AuthorizationExtractor.extract(request);
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new SubwayException(AuthExceptionSet.INVALID_JWT_EXCEPTION);
        }
        return true;
    }
}
