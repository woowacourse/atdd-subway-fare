package wooteco.subway.path.application;

import wooteco.subway.exception.SubwayException;

public class InvalidPathException extends SubwayException {
    private static final String MESSAGE = "유효하지 않은 경로입니다.";

    public InvalidPathException() {
        super(MESSAGE);
    }
}
