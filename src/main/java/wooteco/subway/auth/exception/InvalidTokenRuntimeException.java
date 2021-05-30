package wooteco.subway.auth.exception;

import wooteco.subway.exception.AuthorizationException;

public class InvalidTokenRuntimeException extends AuthorizationException {
    private static final String ERROR_MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenRuntimeException() {
        super(ERROR_MESSAGE);
    }
}
