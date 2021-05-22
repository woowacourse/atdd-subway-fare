package wooteco.subway.member.exception;

import wooteco.subway.exception.BusinessException;
import wooteco.subway.exception.ErrorCode;

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
