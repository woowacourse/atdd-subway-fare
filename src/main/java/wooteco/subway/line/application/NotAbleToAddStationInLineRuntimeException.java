package wooteco.subway.line.application;

import wooteco.subway.exception.SubwayRuntimeException;

public class NotAbleToAddStationInLineRuntimeException extends SubwayRuntimeException {
    private static final String ERROR_MESSAGE = "노선에 역을 등록할 수 없습니다";

    public NotAbleToAddStationInLineRuntimeException() {
        super(ERROR_MESSAGE);
    }
}
