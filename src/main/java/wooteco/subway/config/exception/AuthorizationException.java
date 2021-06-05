package wooteco.subway.config.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends HttpException {

    private static final HttpStatus DEFAULT_STATUS = HttpStatus.UNAUTHORIZED;

    public AuthorizationException() {
        super(DEFAULT_STATUS);
    }

    public AuthorizationException(String message) {
        super(message, DEFAULT_STATUS);
    }
}
