package wooteco.subway.path.application;

import wooteco.subway.exception.SubwayRuntimeException;

public class InvalidPathRuntimeException extends SubwayRuntimeException {
    private static final String ERROR_MESSAGE = "잘못된 경로입니다.";

    public InvalidPathRuntimeException() {
        super(ERROR_MESSAGE);
    }
}
