package wooteco.subway.exception;

public class ValidationFailureException extends RuntimeException {

    public ValidationFailureException(String message) {
        super(message);
    }
}
