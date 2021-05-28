package wooteco.subway.line.exception;

import wooteco.subway.exception.SubwayRuntimeException;

public class NotAbleToDeleteStationInLineRuntimeException extends SubwayRuntimeException {
    private static final String ERROR_MESSAGE = "노선에 포함된 역은 삭제할 수 없습니다.";

    public NotAbleToDeleteStationInLineRuntimeException() {
        super(ERROR_MESSAGE);
    }
}
