package wooteco.auth.web.api;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.auth.domain.AuthenticationPrincipal;
import wooteco.auth.domain.Authority;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.util.AuthorizationExtractor;
import wooteco.auth.util.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        if (!jwtTokenProvider.validateToken(credentials)) {
            return new LoginMember(Authority.ANONYMOUS);
        }

        Long id = Long.parseLong(jwtTokenProvider.getPayload(credentials));

        return new LoginMember(id, Authority.USER);
    }
}
