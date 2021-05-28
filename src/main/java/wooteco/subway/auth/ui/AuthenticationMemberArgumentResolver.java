package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationMember;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationMemberArgumentResolver extends AuthenticationArgumentResolver{

    public AuthenticationMemberArgumentResolver(AuthService authService) {
        super(authService);
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationMember.class);
    }

    @Override
    protected LoginMember validMember(LoginMember member) {
        if (member.getId() == null) {
            return new LoginMember(null, null, 20);
        }
        return member;
    }

}
