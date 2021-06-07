package wooteco.subway.infrastructure.exception.domain.duplicate;

import wooteco.subway.infrastructure.ErrorCode;
import wooteco.subway.infrastructure.exception.domain.SubwayException;

public class StationDuplicatedException extends SubwayException {

    public StationDuplicatedException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
