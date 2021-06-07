package wooteco.subway.infrastructure.exception.domain.delete;

import wooteco.subway.infrastructure.ErrorCode;
import wooteco.subway.infrastructure.exception.domain.SubwayException;

public class SectionDeleteException extends SubwayException {
    public SectionDeleteException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
