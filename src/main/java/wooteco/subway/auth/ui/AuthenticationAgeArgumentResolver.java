package wooteco.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import wooteco.subway.auth.application.AuthService;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.auth.domain.AuthenticationAge;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.LoginMember;

public class AuthenticationAgeArgumentResolver implements HandlerMethodArgumentResolver {
    private AuthService authService;

    public AuthenticationAgeArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationAge.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        if (credentials == null) {
            return 20;
        }

        LoginMember member = authService.findMemberByToken(credentials);
        if (member.getId() == null) {
            throw new AuthorizationException("유효하지 않은 토큰 정보입니다.");
        }
        return member.getAge();
    }
}
