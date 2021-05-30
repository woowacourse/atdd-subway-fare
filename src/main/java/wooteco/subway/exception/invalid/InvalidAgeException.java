package wooteco.subway.exception.invalid;

import wooteco.subway.exception.SubwayException;

public class InvalidAgeException extends SubwayException {
    private static final String ERROR = "INVALID_AGE";

    public InvalidAgeException(String message) {
        super(ERROR, message);
    }
}
