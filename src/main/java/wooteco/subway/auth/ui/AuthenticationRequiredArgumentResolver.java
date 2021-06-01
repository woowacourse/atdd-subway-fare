package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.domain.AuthenticationRequired;
import wooteco.subway.member.domain.LoginMember;

public class AuthenticationRequiredArgumentResolver extends AuthenticationArgumentResolver {

    public AuthenticationRequiredArgumentResolver(AuthService authService) {
        super(authService);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationRequired.class);
    }

    @Override
    LoginMember considerUncertifiedMember(LoginMember member) {
        if (member.isUncertified()) {
            throw new AuthorizationException("토큰 검증에 실패했습니다.");
        }
        return member;
    }
}
