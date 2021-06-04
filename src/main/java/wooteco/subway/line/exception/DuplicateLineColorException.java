package wooteco.subway.line.exception;

import wooteco.subway.exception.BusinessException;
import wooteco.subway.exception.ErrorCode;

public class DuplicateLineColorException extends BusinessException {

    public DuplicateLineColorException() {
        super(ErrorCode.DUPLICATE_LINE_COLOR);
    }
}
