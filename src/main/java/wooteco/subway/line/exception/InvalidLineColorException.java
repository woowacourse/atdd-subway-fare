package wooteco.subway.line.exception;

public class InvalidLineColorException extends IllegalArgumentException {
    public static final String ERROR_MESSAGE = "등록될 수 없는 색상입니다.";

    public InvalidLineColorException() {
        super(ERROR_MESSAGE);
    }
}