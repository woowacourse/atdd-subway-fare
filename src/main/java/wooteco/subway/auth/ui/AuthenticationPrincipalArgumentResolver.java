package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.domain.User;

public class AuthenticationPrincipalArgumentResolver extends AuthenticationArgumentResolver {

    public AuthenticationPrincipalArgumentResolver(AuthService authService) {
        super(authService);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    protected User validMember(User user) {
        if (user.isGuest()) {
            throw new AuthorizationException("토큰 검증에 실패했습니다.");
        }
        return user;
    }

}
