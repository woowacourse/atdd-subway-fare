package wooteco.subway.line.application;

public class NotAbleToAddStationInLineException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "노선에 역을 등록할 수 없습니다";

    public NotAbleToAddStationInLineException() {
        super(ERROR_MESSAGE);
    }
}
