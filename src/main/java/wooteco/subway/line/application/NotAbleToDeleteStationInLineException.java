package wooteco.subway.line.application;

public class NotAbleToDeleteStationInLineException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "노선에 포함된 역은 삭제할 수 없습니다.";

    public NotAbleToDeleteStationInLineException() {
        super(ERROR_MESSAGE);
    }
}
