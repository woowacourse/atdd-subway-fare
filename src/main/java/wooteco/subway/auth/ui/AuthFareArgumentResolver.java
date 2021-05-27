package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationAgePrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.ui.farepolicy.*;

import javax.servlet.http.HttpServletRequest;

public class AuthFareArgumentResolver implements HandlerMethodArgumentResolver {
    private AuthService authService;

    public AuthFareArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationAgePrincipal.class);
    }

    @Override
    public FarePolicy resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        LoginMember member = authService.findMemberByToken(credentials);
        return findProperFarePolicy(member);
    }

    private FarePolicy findProperFarePolicy(LoginMember member) {
        if (member.getId() == null || member.isAdult()) {
            return new AdultFarePolicy();
        }

        if (member.isTeenager()) {
            return new TeenagerFarePolicy();
        }

        if (member.isChild()) {
            return new ChildFarePolicy();
        }

        return new InfantFarePolicy();
    }
}
