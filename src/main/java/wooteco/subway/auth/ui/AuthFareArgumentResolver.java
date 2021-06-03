package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationAgePrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.line.domain.fare.AgeFarePolicy;
import wooteco.subway.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;

public class AuthFareArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;

    public AuthFareArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationAgePrincipal.class);
    }

    @Override
    public AgeFarePolicy resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        LoginMember member = authService.findMemberByToken(credentials);
        return findProperFarePolicy(member);
    }

    private AgeFarePolicy findProperFarePolicy(LoginMember member) {
        if (member.getId() == null) {
            return AgeFarePolicy.ADULT;
        }

        return AgeFarePolicy.of(member.getAge());
    }
}
