package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends SubwayException {

    private static final String AUTHORIZATION_MESSAGE = "인증되지 않은 사용자입니다.";

    public AuthorizationException() {
        super(HttpStatus.UNAUTHORIZED, AUTHORIZATION_MESSAGE);
    }
}
