package wooteco.subway.line.exception;

public class DuplicateLineColorException extends RuntimeException {
    private static final String ERROR_MESSAGE = "이미 등록된 노선 색상입니다.";

    public DuplicateLineColorException() {
        super(ERROR_MESSAGE);
    }
}