package wooteco.subway.auth.config;

import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.exception.AuthException;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.SubwayCustomException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    private JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String extractor = AuthorizationExtractor.extract(request);

        if(!jwtTokenProvider.validateToken(extractor)) {
            throw new SubwayCustomException(AuthException.INVALID_TOKEN_EXCEPTION);
        }

        return true;
    }
}
