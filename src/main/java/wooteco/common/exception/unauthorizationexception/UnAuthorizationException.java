package wooteco.common.exception.unauthorizationexception;

public class UnAuthorizationException extends RuntimeException {

    public UnAuthorizationException() {
        super("인증되지 않은 사용자입니다.");
    }
}
