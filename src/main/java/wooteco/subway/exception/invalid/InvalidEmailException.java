package wooteco.subway.exception.invalid;

import wooteco.subway.exception.SubwayException;

public class InvalidEmailException extends SubwayException {
    private static final String ERROR = "INVALID_EMAIL";

    public InvalidEmailException(String message) {
        super(ERROR, message);
    }
}
