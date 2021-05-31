package wooteco.subway.exception.auth;

import wooteco.subway.exception.BusinessException;
import wooteco.subway.exception.ExceptionStatus;

public class AuthorizationException extends BusinessException {

    public AuthorizationException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
