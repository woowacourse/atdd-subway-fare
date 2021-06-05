package wooteco.subway.exception.notfound;

import wooteco.subway.exception.BusinessException;
import wooteco.subway.exception.ExceptionStatus;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
