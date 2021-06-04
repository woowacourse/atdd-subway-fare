package wooteco.subway.auth.exception;

import wooteco.subway.config.exception.AuthorizationException;

public class InvalidTokenException extends AuthorizationException {
    public static final String ERROR_MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(ERROR_MESSAGE);
    }
}
