package wooteco.subway.line.exception;

import wooteco.subway.exception.BusinessException;
import wooteco.subway.exception.ErrorCode;

public class DuplicateLineNameException extends BusinessException {

    public DuplicateLineNameException() {
        super(ErrorCode.DUPLICATE_LINE_NAME);
    }
}
