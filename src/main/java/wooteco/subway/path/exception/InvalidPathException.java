package wooteco.subway.path.exception;

import wooteco.subway.config.exception.BadRequestException;

public class InvalidPathException extends BadRequestException {
    private static final String ERROR_MESSAGE = "잘못된 경로입니다.";

    public InvalidPathException() {
        super(ERROR_MESSAGE);
    }
}
