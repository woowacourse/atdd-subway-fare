package wooteco.common.exception.unauthorizationexception;

public class UnAuthorizationException extends RuntimeException {
    public UnAuthorizationException() {
    }

    public UnAuthorizationException(String message) {
        super(message);
    }
}
