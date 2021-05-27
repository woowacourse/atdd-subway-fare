package wooteco.subway.line.application;

import wooteco.subway.exception.SubwayRuntimeException;

public class SameUpAndDownStationRuntimeException extends SubwayRuntimeException {
    private static final String ERROR_MESSAGE = "상행 종점, 하행 종점은 같을 수 없습니다.";

    public SameUpAndDownStationRuntimeException() {
        super(ERROR_MESSAGE);
    }
}
