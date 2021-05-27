package wooteco.subway.exception;

public class CannotRemoveStationException extends RuntimeException {

    private static final String CANNOT_REMOVE_STATION_MESSAGE = "해당 역이 등록된 구간이 있어 삭제할 수 없습니다.";

    public CannotRemoveStationException() {
        super(CANNOT_REMOVE_STATION_MESSAGE);
    }
}
