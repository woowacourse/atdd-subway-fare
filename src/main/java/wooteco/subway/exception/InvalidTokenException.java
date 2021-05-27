package wooteco.subway.exception;

public class InvalidTokenException extends SubwayException {
    private static final String ERROR = "INVALID_TOKEN";
    private static final String MESSAGE = "토큰이 유효하지 않습니다.";

    public InvalidTokenException() {
        super(ERROR, MESSAGE);
    }
}
