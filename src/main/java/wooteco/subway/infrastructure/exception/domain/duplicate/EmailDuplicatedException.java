package wooteco.subway.infrastructure.exception.domain.duplicate;

import wooteco.subway.infrastructure.ErrorCode;
import wooteco.subway.infrastructure.exception.domain.SubwayException;

public class EmailDuplicatedException extends SubwayException {

    public EmailDuplicatedException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
