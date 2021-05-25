package wooteco.common.exception.unauthorizationexception;

public class AuthorizationException extends UnAuthorizationException {

    public AuthorizationException() {
        super("인증되지 않은 사용자입니다.");
    }
}
