package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;

public abstract class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public AuthenticationArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public LoginMember resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String credentials = AuthorizationExtractor
                .extract(webRequest.getNativeRequest(HttpServletRequest.class));
        LoginMember member = authService.findMemberByToken(credentials);
        return validMember(member);
    }

    protected abstract LoginMember validMember(LoginMember member);
}
