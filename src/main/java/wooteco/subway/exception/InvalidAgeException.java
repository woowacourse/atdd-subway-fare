package wooteco.subway.exception;

public class InvalidAgeException extends SubwayException {
    private static final String ERROR = "INVALID_AGE";

    public InvalidAgeException(String message) {
        super(ERROR, message);
    }
}
