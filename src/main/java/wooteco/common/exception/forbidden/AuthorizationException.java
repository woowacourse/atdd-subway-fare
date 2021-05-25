package wooteco.common.exception.forbidden;

public class AuthorizationException extends ForbiddenException {

    public AuthorizationException() {
        super("토큰 인증 오류");
    }
}
