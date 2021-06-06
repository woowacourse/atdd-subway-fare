package wooteco.subway.infrastructure.exception.domain.delete;

import wooteco.subway.infrastructure.ErrorCode;
import wooteco.subway.infrastructure.exception.domain.SubwayException;

public class StationDeleteException extends SubwayException {

    public StationDeleteException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
