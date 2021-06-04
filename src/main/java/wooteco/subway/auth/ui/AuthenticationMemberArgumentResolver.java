package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationMember;
import wooteco.subway.member.domain.GuestUser;
import wooteco.subway.member.domain.User;

public class AuthenticationMemberArgumentResolver extends AuthenticationArgumentResolver {

    public AuthenticationMemberArgumentResolver(AuthService authService) {
        super(authService);
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationMember.class);
    }

    @Override
    protected User validMember(User user) {
        if (user.isGuest()) {
            return new GuestUser();
        }
        return user;
    }

}
