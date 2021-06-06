package wooteco.subway.line.exception;

public class LineNotFoundException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "노선이 등록되어 있지 않습니다.";

    public LineNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
