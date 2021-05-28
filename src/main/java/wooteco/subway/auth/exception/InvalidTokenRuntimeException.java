package wooteco.subway.auth.exception;

import wooteco.subway.exception.SubwayAuthorizationException;

public class InvalidTokenRuntimeException extends SubwayAuthorizationException {
    private static final String ERROR_MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenRuntimeException() {
        super(ERROR_MESSAGE);
    }
}
