package wooteco.subway.line.exception;

public class DuplicateLineNameException extends RuntimeException {
    private static final String ERROR_MESSAGE = "이미 등록된 노선 이름입니다.";

    public DuplicateLineNameException() {
        super(ERROR_MESSAGE);
    }
}

