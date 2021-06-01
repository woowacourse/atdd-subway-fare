package wooteco.subway.auth.infrastructure;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import wooteco.subway.auth.application.AuthService;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.member.domain.LoginMember;

public class UnAuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private static final int DEFAULT_AGE = 20;

    private final AuthService authService;

    public UnAuthenticationPrincipalArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UnAuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(
            Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)));
        try{
            return authService.findMemberByToken(credentials);
        } catch (SubwayException e){
            return new LoginMember(DEFAULT_AGE);
        }
    }
}
