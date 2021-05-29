package wooteco.subway.auth.application;

public class AuthorizationException extends RuntimeException {
    private static final String ERROR_MESSAGE = "로그인 후 사용해주세요.";

    public AuthorizationException() {
        super(ERROR_MESSAGE);
    }
}
