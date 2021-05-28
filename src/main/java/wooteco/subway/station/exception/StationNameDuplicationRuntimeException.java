package wooteco.subway.station.exception;

import wooteco.subway.exception.SubwayRuntimeException;

public class StationNameDuplicationRuntimeException extends SubwayRuntimeException {
    private static final String ERROR_MESSAGE = "이미 존재하는 역 이름 입니다.";

    public StationNameDuplicationRuntimeException() {
        super(ERROR_MESSAGE);
    }
}
