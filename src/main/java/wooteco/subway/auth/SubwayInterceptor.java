package wooteco.subway.auth;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class SubwayInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public SubwayInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflightRequest(request)) {
            return true;
        }
        if (isGet(request) && isNotMembersMe(request)) {
            return true;
        }
        String token = AuthorizationExtractor.extract(request);
        jwtTokenProvider.validateToken(token);
        return true;
    }

    private boolean isPreflightRequest(HttpServletRequest request) {
        return isOptions(request) && hasHeaders(request) && hasMethod(request) && hasOrigin(request);
    }

    private boolean isOptions(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.toString());
    }

    private boolean hasHeaders(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Access-Control-Request-Headers"));
    }

    private boolean hasMethod(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Access-Control-Request-Method"));
    }

    private boolean hasOrigin(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Origin"));
    }

    private boolean isGet(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase(HttpMethod.GET.toString());
    }

    private boolean isNotMembersMe(HttpServletRequest request) {
        return !request.getRequestURI().equalsIgnoreCase("/api/members/me");
    }
}
