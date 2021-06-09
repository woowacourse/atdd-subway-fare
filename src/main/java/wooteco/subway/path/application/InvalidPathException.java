package wooteco.subway.path.application;

import wooteco.subway.exception.web.BadRequestException;

public class InvalidPathException extends BadRequestException {

    public InvalidPathException(String message) {
        super(message);
    }
}
