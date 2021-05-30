package wooteco.subway.line.exception;

import wooteco.subway.exception.SubwayException;

public class LineNotExistException extends SubwayException {
    private static final String ERROR_MESSAGE = "조회하려는 노선이 없습니다.";

    public LineNotExistException() {
        super(ERROR_MESSAGE);
    }
}
