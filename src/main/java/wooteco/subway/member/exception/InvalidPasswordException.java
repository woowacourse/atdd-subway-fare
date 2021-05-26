package wooteco.subway.member.exception;

import wooteco.subway.exception.BusinessException;
import wooteco.subway.exception.ErrorCode;

public class InvalidPasswordException extends BusinessException {

    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD);
    }
}
