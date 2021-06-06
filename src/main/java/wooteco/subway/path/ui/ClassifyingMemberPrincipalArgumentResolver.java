package wooteco.subway.path.ui;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.ClassifyingMemberPrincipal;

public class ClassifyingMemberPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public ClassifyingMemberPrincipalArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ClassifyingMemberPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Objects.requireNonNull(request);

        if (!AuthorizationExtractor.isExtractable(request)) {
            return LoginMember.empty();
        }

        String credentials = AuthorizationExtractor.extract(request);

        return authService.findMemberByToken(credentials);
    }

}
