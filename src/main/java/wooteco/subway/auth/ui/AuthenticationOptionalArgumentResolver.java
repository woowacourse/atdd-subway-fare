package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationOptional;
import wooteco.subway.member.domain.LoginMember;

public class AuthenticationOptionalArgumentResolver extends AuthenticationArgumentResolver {

    public AuthenticationOptionalArgumentResolver(AuthService authService) {
        super(authService);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationOptional.class);
    }

    @Override
    protected LoginMember considerGuest(LoginMember member) {
        return member;
    }
}
