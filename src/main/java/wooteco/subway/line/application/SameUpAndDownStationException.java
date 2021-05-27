package wooteco.subway.line.application;

public class SameUpAndDownStationException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "상행 종점, 하행 종점은 같을 수 없습니다.";

    public SameUpAndDownStationException() {
        super(ERROR_MESSAGE);
    }
}
