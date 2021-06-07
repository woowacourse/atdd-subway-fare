package wooteco.auth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.auth.util.AuthorizationExtractor;
import wooteco.auth.util.JwtTokenProvider;
import wooteco.common.exception.forbidden.AuthorizationException;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
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
