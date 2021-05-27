package wooteco.subway.station.application;

public class DuplicateStationNameException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "이미 등록된 역입니다.";

    public DuplicateStationNameException() {
        super(ERROR_MESSAGE);
    }
}
