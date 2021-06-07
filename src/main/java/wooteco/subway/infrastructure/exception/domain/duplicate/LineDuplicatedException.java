package wooteco.subway.infrastructure.exception.domain.duplicate;

import wooteco.subway.infrastructure.ErrorCode;
import wooteco.subway.infrastructure.exception.domain.SubwayException;

public class LineDuplicatedException extends SubwayException {

    public LineDuplicatedException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
