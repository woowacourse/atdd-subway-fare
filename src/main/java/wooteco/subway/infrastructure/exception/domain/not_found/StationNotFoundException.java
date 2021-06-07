package wooteco.subway.infrastructure.exception.domain.not_found;

import wooteco.subway.infrastructure.ErrorCode;
import wooteco.subway.infrastructure.exception.domain.SubwayException;

public class StationNotFoundException extends SubwayException {

    public StationNotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
