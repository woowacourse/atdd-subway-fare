package wooteco.subway.line.application;

import wooteco.subway.exception.SubwayException;

public class NoSuchSectionException extends SubwayException {
    private static final String message = "존재하지 않는 구간입니다.";

    public NoSuchSectionException() {
        super(message);
    }
}
