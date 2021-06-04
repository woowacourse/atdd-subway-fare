package wooteco.subway.line.exception;

import wooteco.subway.exception.BusinessException;
import wooteco.subway.exception.ErrorCode;

public class InvalidSectionException extends BusinessException {

    public InvalidSectionException(String message) {
        super(ErrorCode.INVALID_SECTION, message);
    }
}
