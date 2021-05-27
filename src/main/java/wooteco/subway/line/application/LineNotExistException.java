package wooteco.subway.line.application;

public class LineNotExistException extends RuntimeException {
    private static final String ERROR_MESSAGE = "조회하려는 노선이 없습니다.";

    public LineNotExistException() {
        super(ERROR_MESSAGE);
    }
}
