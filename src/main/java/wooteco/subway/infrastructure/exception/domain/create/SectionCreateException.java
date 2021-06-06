package wooteco.subway.infrastructure.exception.domain.create;

import wooteco.subway.infrastructure.ErrorCode;
import wooteco.subway.infrastructure.exception.domain.SubwayException;

public class SectionCreateException extends SubwayException {

    public SectionCreateException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
