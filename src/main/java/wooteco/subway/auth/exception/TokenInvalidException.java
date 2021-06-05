package wooteco.subway.auth.exception;

import wooteco.subway.exception.AuthorizationException;

public class TokenInvalidException extends AuthorizationException {
    public TokenInvalidException() {
    }

    public TokenInvalidException(String message) {
        super(message);
    }
}
