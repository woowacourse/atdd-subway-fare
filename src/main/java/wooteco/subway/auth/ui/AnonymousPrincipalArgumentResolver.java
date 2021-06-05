package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AnonymousPrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.AnonymousUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class AnonymousPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public AnonymousPrincipalArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AnonymousPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String credentials = AuthorizationExtractor.extract(httpRequest);
        if (Objects.isNull(credentials)) {
            return new AnonymousUser();
        }
        return authService.findMemberByToken(credentials);
    }
}
