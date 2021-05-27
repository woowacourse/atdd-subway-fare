package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public AuthenticationPrincipalArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest httpRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String credentials = AuthorizationExtractor.extract(httpRequest);
        if (isAnonymousPermissible(httpRequest, credentials)) {
            return null;
        }
        return authService.findMemberByToken(credentials);
    }

    private boolean isAnonymousPermissible(HttpServletRequest httpRequest, String credentials) {
        return Objects.isNull(credentials) && "/paths".equals(httpRequest.getRequestURI());
    }
}
