package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.MustLoginPrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.config.exception.AuthorizationException;
import wooteco.subway.member.domain.AuthMember;
import wooteco.subway.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;

public class MustLoginPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;

    public MustLoginPrincipalArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MustLoginPrincipal.class);
    }

    @Override
    public LoginMember resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        AuthMember member = authService.findMemberByToken(credentials);
        if (member.isLoggedIn()) {
            return (LoginMember) member;
        }
        throw new AuthorizationException();
    }
}
