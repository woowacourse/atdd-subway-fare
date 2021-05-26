package wooteco.subway.auth.exception;

public class InvalidTokenException extends RuntimeException {
    private static final String MESSAGE = "INVALID_TOKEN";

    public InvalidTokenException() {
        super(MESSAGE);
    }
}
