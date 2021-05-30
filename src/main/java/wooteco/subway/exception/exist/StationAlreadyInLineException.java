package wooteco.subway.exception.exist;

import wooteco.subway.exception.SubwayException;

public class StationAlreadyInLineException extends SubwayException {
    private static final String ERROR = "STATION_ALREADY_REGISTERED_IN_LINE";
    private static final String MESSAGE = "노선에 등록된 역은 삭제할 수 없습니다.";

    public StationAlreadyInLineException() {
        super(ERROR, MESSAGE);
    }
}
