package wooteco.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.Guest;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.User;

public class AuthenticationMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private AuthService authService;

    public AuthenticationMemberArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor
            .extract(webRequest.getNativeRequest(HttpServletRequest.class));
        LoginMember member = authService.findMemberByToken(credentials);
        return validMember(member);
    }

    public User validMember(LoginMember member) {
        if (member.getId() == null) {
            return new Guest();
        }
        return member;
    }
}
