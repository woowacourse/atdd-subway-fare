package wooteco.subway.line.exception;

import wooteco.subway.exception.InvalidRequestException;

public class InvalidSectionRequestException extends InvalidRequestException {
    public InvalidSectionRequestException() {
    }

    public InvalidSectionRequestException(String message) {
        super(message);
    }
}
