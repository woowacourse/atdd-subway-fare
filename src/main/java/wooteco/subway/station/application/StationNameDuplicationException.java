package wooteco.subway.station.application;

public class StationNameDuplicationException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "이미 존재하는 역 이름 입니다.";

    public StationNameDuplicationException() {
        super(ERROR_MESSAGE);
    }
}
