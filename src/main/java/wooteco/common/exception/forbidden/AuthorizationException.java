package wooteco.common.exception.forbidden;

public class AuthorizationException extends RuntimeException {

    private static final String MESSAGE = "잘못된 인증 정보입니다.";

    public AuthorizationException() {
        super(MESSAGE);
    }
}
