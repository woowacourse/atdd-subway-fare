package wooteco.subway.exception;

public class InvalidInputException extends SubwayException{
    private static final String ERROR = "INVALID_INPUT";
    private static final String MESSAGE = "아이디/비밀번호 입력이 올바르지 않습니다.";

    public InvalidInputException() {
        super(ERROR, MESSAGE);
    }

    public InvalidInputException(String message) {
        super(ERROR, message);
    }
}
