package wooteco.subway.station.application;

import wooteco.subway.exception.SubwayRuntimeException;

public class StationNotExistRuntimeException extends SubwayRuntimeException {
    private static final String ERROR_MESSAGE = "존재하지 않는 역입니다.";

    public StationNotExistRuntimeException() {
        super(ERROR_MESSAGE);
    }
}
