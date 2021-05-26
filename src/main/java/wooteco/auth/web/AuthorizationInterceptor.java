package wooteco.auth.web;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.auth.infrastructure.AuthorizationExtractor;
import wooteco.auth.infrastructure.JwtTokenProvider;
import wooteco.common.exception.forbidden.AuthorizationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthorizationInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String method = request.getMethod();
        if (method.equals(HttpMethod.GET.name()) || method.equals(HttpMethod.OPTIONS.name())) {
            return true;
        }
        final String credentials = AuthorizationExtractor.extract(request);
        if (credentials == null || !jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException();
        }
        return true;
    }
}
