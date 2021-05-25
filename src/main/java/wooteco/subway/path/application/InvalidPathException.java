package wooteco.subway.path.application;

import wooteco.subway.exception.BadRequestException;

public class InvalidPathException extends BadRequestException {
    private static final String message = "유효하지 않은 경로입니다.";

    public InvalidPathException() {
        super(message);
    }
}
