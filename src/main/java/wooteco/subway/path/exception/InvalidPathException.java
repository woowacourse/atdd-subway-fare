package wooteco.subway.path.exception;

import wooteco.subway.exception.SubwayException;

public class InvalidPathException extends SubwayException {
    private static final String ERROR_MESSAGE = "잘못된 경로입니다.";

    public InvalidPathException() {
        super(ERROR_MESSAGE);
    }
}
