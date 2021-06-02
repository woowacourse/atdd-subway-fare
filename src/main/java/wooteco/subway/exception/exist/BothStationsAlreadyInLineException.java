package wooteco.subway.exception.exist;

import wooteco.subway.exception.SubwayException;

public class BothStationsAlreadyInLineException extends SubwayException {
    private static final String ERROR = "BOTH_STATION_ALREADY_REGISTERED_IN_LINE";
    private static final String MESSAGE = "이미 두 역이 노선에 존재합니다.";

    public BothStationsAlreadyInLineException() {
        super(ERROR, MESSAGE);
    }
}