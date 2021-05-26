package wooteco.subway.auth.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.exception.SubwayAuthException;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.SubwayCustomException;

public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null) {
            throw new SubwayCustomException(SubwayAuthException.NOT_EXIST_TOKEN_EXCEPTION);
        }

        String credentials = AuthorizationExtractor.extract(request);
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new SubwayCustomException(SubwayAuthException.INVALID_JWT_EXCEPTION);
        }
        return true;
    }
}
