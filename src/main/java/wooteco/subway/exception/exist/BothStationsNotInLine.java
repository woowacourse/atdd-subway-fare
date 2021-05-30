package wooteco.subway.exception.exist;

import wooteco.subway.exception.SubwayException;

public class BothStationsNotInLine extends SubwayException {
    private static final String ERROR = "BOTH_STATION_NOT_REGISTERED_IN_LINE";
    private static final String MESSAGE = "두 역이 노선에 이미 존재하지 않습니다.";

    public BothStationsNotInLine() {
        super(ERROR, MESSAGE);
    }
}
