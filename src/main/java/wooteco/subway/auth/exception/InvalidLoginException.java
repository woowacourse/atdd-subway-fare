package wooteco.subway.auth.exception;

import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.ErrorCode;

public class InvalidLoginException extends AuthorizationException {

    public InvalidLoginException() {
        super(ErrorCode.INVALID_LOGIN);
    }
}
