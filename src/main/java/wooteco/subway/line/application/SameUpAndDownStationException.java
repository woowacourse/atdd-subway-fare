package wooteco.subway.line.application;

import wooteco.subway.exception.SubwayException;

public class SameUpAndDownStationException extends SubwayException {
    private static final String MESSAGE = "상행역과 하행역은 동일할 수 없습니다.";

    public SameUpAndDownStationException() {
        super(MESSAGE);
    }
}
