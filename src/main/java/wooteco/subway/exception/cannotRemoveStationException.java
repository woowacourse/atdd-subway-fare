package wooteco.subway.exception;

public class cannotRemoveStationException extends RuntimeException {

    private static final String CANNOT_REMOVE_STATION_MESSAGE = "해당 역이 등록된 구간이 있어 삭제할 수 없습니다.";

    public cannotRemoveStationException() {
        super(CANNOT_REMOVE_STATION_MESSAGE);
    }
}
