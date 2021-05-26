package wooteco.subway.exception;

public class InvalidNameException extends SubwayException{
    private static final String ERROR = "INVALID_NAME";
    private static final String MESSAGE = "이름 입력이 올바르지 않습니다.";

    public InvalidNameException() {
        super(ERROR, MESSAGE);
    }

    public InvalidNameException(String message) {
        super(ERROR, message);
    }
}
