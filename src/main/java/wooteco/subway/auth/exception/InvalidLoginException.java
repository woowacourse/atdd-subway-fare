package wooteco.subway.auth.exception;

import wooteco.subway.exception.BusinessException;
import wooteco.subway.exception.ErrorCode;

public class InvalidLoginException extends BusinessException {

    public InvalidLoginException() {
        super(ErrorCode.INVALID_LOGIN);
    }
}
