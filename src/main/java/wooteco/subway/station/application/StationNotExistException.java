package wooteco.subway.station.application;

public class StationNotExistException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "존재하지 않는 역입니다.";

    public StationNotExistException() {
        super(ERROR_MESSAGE);
    }
}
