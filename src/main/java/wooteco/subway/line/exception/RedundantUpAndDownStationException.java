package wooteco.subway.line.exception;

import wooteco.subway.exception.SubwayException;

public class RedundantUpAndDownStationException extends SubwayException {
    private static final String ERROR_MESSAGE = "상행 종점, 하행 종점은 같을 수 없습니다.";

    public RedundantUpAndDownStationException() {
        super(ERROR_MESSAGE);
    }
}
