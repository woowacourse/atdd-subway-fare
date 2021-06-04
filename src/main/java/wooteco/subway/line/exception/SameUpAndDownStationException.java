package wooteco.subway.line.exception;

import wooteco.subway.config.exception.BadRequestException;

public class SameUpAndDownStationException extends BadRequestException {
    private static final String ERROR_MESSAGE = "상행 종점, 하행 종점은 같을 수 없습니다.";

    public SameUpAndDownStationException() {
        super(ERROR_MESSAGE);
    }
}
