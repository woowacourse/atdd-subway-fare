package wooteco.subway.exception;

public class AuthorizationException extends RuntimeException {
    private String message;

    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
