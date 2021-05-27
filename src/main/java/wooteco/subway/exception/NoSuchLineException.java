package wooteco.subway.exception;

public class NoSuchLineException extends SubwayException {
    private static final String ERROR = "NO_SUCH_LINE";
    private static final String MESSAGE = "해당 노선이 존재하지 않습니다.";

    public NoSuchLineException() {
        super(ERROR, MESSAGE);
    }
}
