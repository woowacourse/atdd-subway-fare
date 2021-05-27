package wooteco.subway.station.application;

public class StationNotFoundException  extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "역이 등록되어 있지 않습니다.";

    public StationNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
