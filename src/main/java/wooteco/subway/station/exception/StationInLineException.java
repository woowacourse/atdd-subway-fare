package wooteco.subway.station.exception;

public class StationInLineException extends RuntimeException {
    public static final String ERROR_MESSAGE = "노선에 등록된 역은 삭제할 수 없습니다.";

    public StationInLineException() {
        super(ERROR_MESSAGE);
    }
}
