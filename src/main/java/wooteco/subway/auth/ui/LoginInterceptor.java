package wooteco.subway.auth.ui;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.application.AuthorizationException;

public class LoginInterceptor implements HandlerInterceptor {

    private static final String ORIGIN = "Origin";
    private static final String ACCESS_REQUEST_METHOD = "Access-Control-Request-Method";
    private static final String ACCESS_REQUEST_HEADERS = "Access-Control-Request-Headers";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        if (isPreFlight(request)) {
            return true;
        }

        String accessToken = request.getHeader("Authorization");
        if (accessToken == null) {
            throw new AuthorizationException();
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean isPreFlight(HttpServletRequest request) {
        return isOptions(request)
            && hasOrigin(request)
            && hasRequestHeaders(request)
            && hasRequestMethods(request);
    }

    private boolean isOptions(HttpServletRequest request) {
        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            return true;
        }
        return false;
    }

    public boolean hasOrigin(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader(ORIGIN));
    }

    public boolean hasRequestMethods(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader(ACCESS_REQUEST_METHOD));
    }

    public boolean hasRequestHeaders(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader(ACCESS_REQUEST_HEADERS));
    }
}
