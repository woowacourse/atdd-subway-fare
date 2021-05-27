package wooteco.subway.exception;

public class InvalidDistanceException extends SubwayException {
    private static final String ERROR = "INVALID_DISTANCE";

    public InvalidDistanceException(String message) {
        super(ERROR, message);
    }
}
