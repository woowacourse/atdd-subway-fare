package wooteco.subway.exception;

public class NoAnyLineException extends SubwayException {
    private static final String ERROR = "NO_ANY_LINE";
    private static final String MESSAGE = "조회할 노선이 존재하지 않습니다.";

    public NoAnyLineException() {
        super(ERROR, MESSAGE);
    }
}