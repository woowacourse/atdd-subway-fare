package wooteco.subway.exception;

public class AuthorizationException extends RuntimeException {

    private static final String AUTHORIZATION_MESSAGE = "인증되지 않은 사용자입니다.";

    public AuthorizationException() {
        super(AUTHORIZATION_MESSAGE);
    }
}
