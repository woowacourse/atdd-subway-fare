package wooteco.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.exception.InvalidTokenException;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.LoginMember;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private AuthService authService;

    public AuthenticationPrincipalArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String credentials = AuthorizationExtractor.extract(request);
        LoginMember member = authService.findMemberByToken(credentials);
        if (!isGuest(member)) {
            return member;
        }
        if (isFindPathRequest(request)) {
            return LoginMember.DUMMY;
        }
        throw new InvalidTokenException();
    }

    private boolean isGuest(LoginMember loginMember) {
        return loginMember.getId() == null;
    }

    private boolean isFindPathRequest(HttpServletRequest request) {
        return request.getRequestURI().equals("/api/paths");
    }
}
