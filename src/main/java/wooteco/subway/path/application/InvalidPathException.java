package wooteco.subway.path.application;

import wooteco.subway.exception.InvalidRequestException;

public class InvalidPathException extends InvalidRequestException {

    public InvalidPathException() {
    }

    public InvalidPathException(String message) {
        super(message);
    }
}
