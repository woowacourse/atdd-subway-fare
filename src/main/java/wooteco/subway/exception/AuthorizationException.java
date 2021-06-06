package wooteco.subway.exception;

public class AuthorizationException extends SubwayException {
    private static final String MESSAGE = "인증에 에러가 발생했습니다.";

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException() {
        this(MESSAGE);
    }
}
