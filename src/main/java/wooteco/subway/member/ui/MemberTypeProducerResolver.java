package wooteco.subway.member.ui;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.MemberType;
import wooteco.subway.member.domain.MemberTypeProducer;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class MemberTypeProducerResolver implements HandlerMethodArgumentResolver {
    private AuthService authService;

    public MemberTypeProducerResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberTypeProducer.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        Optional<LoginMember> memberByToken = authService.findMemberByToken(credentials);
        return MemberType.of(memberByToken);
    }
}
