package wooteco.subway.line.application;

import wooteco.subway.exception.SubwayRuntimeException;

public class LineNotExistRuntimeException extends SubwayRuntimeException {
    private static final String ERROR_MESSAGE = "조회하려는 노선이 없습니다.";

    public LineNotExistRuntimeException() {
        super(ERROR_MESSAGE);
    }
}
