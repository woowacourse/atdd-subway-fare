package wooteco.subway.auth.ui;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.exception.InvalidTokenException;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.RequestUser;

public class AuthInterceptor implements HandlerInterceptor {
    private static final String PATH_REQUEST_URI = "/api/paths";

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflightRequest(request)) {
            return true;
        }

        if (isNotFindPathRequest(request) && isNotGetMemberInfoRequest(request) && isGetRequest(request)) {
            return true;
        }

        String credentials = AuthorizationExtractor.extract(request);
        if (isNotFindPathRequest(request) && !authService.validateToken(credentials)) {
            throw new InvalidTokenException();
        }
        request.setAttribute("credentials", credentials);

        return true;
    }

    private boolean isPreflightRequest(HttpServletRequest request) {
        return isOptions(request) && hasAccessControlRequestHeaders(request) && hasAccessControlRequestMethod(request)
            && hasOrigin(request);
    }

    private boolean isOptions(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.toString());
    }

    private boolean hasAccessControlRequestHeaders(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Access-Control-Request-Headers"));
    }

    private boolean hasAccessControlRequestMethod(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Access-Control-Request-Method"));
    }

    private boolean hasOrigin(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Origin"));
    }

    private boolean isGetRequest(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.GET.name());
    }

    private boolean isNotFindPathRequest(HttpServletRequest request) {
        return !request.getRequestURI().equals(PATH_REQUEST_URI);
    }

    private boolean isNotGetMemberInfoRequest(HttpServletRequest request) {
        return !request.getRequestURI().equals("/api/members/me");
    }
}
