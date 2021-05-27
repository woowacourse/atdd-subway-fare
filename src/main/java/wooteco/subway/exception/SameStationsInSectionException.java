package wooteco.subway.exception;

public class SameStationsInSectionException extends SubwayException {
    private static final String ERROR = "SAME_STATIONS_IN_SAME_SECTION";
    private static final String MESSAGE = "같은 역을 한 구간에 등록할 수 없습니다.";

    public SameStationsInSectionException() {
        super(ERROR, MESSAGE);
    }
}