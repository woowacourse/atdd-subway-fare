package wooteco.subway.line.exception;

import wooteco.subway.exception.ErrorCode;
import wooteco.subway.exception.NotFoundException;

public class LineNotFoundException extends NotFoundException {

    public LineNotFoundException() {
        super(ErrorCode.NOTFOUND_LINE);
    }
}
