package wooteco.subway.auth.exception;

import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.ErrorCode;

public class InvalidTokenException extends AuthorizationException {

    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
