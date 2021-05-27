package wooteco.subway.auth.application;

public class InvalidTokenException extends RuntimeException {
    private static final String ERROR_MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(ERROR_MESSAGE);
    }
}
