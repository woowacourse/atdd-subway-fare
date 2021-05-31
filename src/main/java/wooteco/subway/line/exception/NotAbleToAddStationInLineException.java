package wooteco.subway.line.exception;

import wooteco.subway.exception.SubwayException;

public class NotAbleToAddStationInLineException extends SubwayException {
    private static final String ERROR_MESSAGE = "노선에 역을 등록할 수 없습니다";

    public NotAbleToAddStationInLineException() {
        super(ERROR_MESSAGE);
    }
}
