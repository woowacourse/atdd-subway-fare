package wooteco.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.LoginMember;

public abstract class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public AuthenticationArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(
            (HttpServletRequest) webRequest.getNativeRequest()
        );
        LoginMember loginMember = authService.findMemberByToken(credentials);
        return considerGuest(loginMember);
    }

    protected abstract LoginMember considerGuest(LoginMember member);

}
