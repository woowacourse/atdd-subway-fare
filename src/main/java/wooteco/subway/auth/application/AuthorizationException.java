package wooteco.subway.auth.application;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(String.format("인증에 실패했습니다. (%s)", message));
    }
}
