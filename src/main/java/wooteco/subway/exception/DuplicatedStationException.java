package wooteco.subway.exception;

public class DuplicatedStationException extends SubwayException {
    private static final String ERROR = "DUPLICATED_STATION_NAME";
    private static final String MESSAGE = "중복되는 역 이름이 존재합니다.";

    public DuplicatedStationException() {
        super(ERROR, MESSAGE);
    }
}
