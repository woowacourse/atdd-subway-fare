package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.domain.LoginMember;

public class AuthenticationPrincipalArgumentResolver extends AuthenticationArgumentResolver {

    public AuthenticationPrincipalArgumentResolver(AuthService authService) {
        super(authService);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    protected LoginMember validMember(LoginMember member) {
        if (member.getId() == null) {
            throw new AuthorizationException("토큰 검증에 실패했습니다.");
        }
        return member;
    }

}
