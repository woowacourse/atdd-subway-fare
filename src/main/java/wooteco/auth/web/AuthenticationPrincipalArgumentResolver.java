package wooteco.auth.web;

import io.jsonwebtoken.MalformedJwtException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.domain.Role;
import wooteco.auth.util.AuthorizationExtractor;
import wooteco.auth.util.JwtTokenProvider;
import wooteco.common.exception.forbidden.AuthorizationException;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
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
        if (credentials == null) {
            return LoginMember.anonymous();
        }
        try {
            final long memberId = Long.parseLong(jwtTokenProvider.getPayload(credentials));
            return new LoginMember(memberId, Role.USER);
        } catch (MalformedJwtException e) {
            throw new AuthorizationException();
        }

    }
}
