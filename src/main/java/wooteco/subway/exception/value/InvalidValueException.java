package wooteco.subway.exception.value;

import wooteco.subway.exception.BusinessException;
import wooteco.subway.exception.ExceptionStatus;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
