package wooteco.subway.exception;

public class InvalidPasswordException extends SubwayException {
    private static final String ERROR = "INVALID_PASSWORD";

    public InvalidPasswordException(String message) {
        super(ERROR, message);
    }
}
