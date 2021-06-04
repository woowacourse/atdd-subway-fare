package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends SubwayException {

    public AuthorizationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
