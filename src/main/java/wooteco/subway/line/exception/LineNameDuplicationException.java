package wooteco.subway.line.exception;

import wooteco.subway.exception.SubwayException;

public class LineNameDuplicationException extends SubwayException {
    private static final String ERROR_MESSAGE = "이미 존재하는 노선 이름 입니다.";

    public LineNameDuplicationException() {
        super(ERROR_MESSAGE);
    }
}
