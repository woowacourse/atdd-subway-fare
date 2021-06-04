package wooteco.subway.station.exception;

import wooteco.subway.exception.BusinessException;
import wooteco.subway.exception.ErrorCode;

public class DuplicateStationNameException extends BusinessException {

    public DuplicateStationNameException() {
        super(ErrorCode.DUPLICATE_STATION_NAME);
    }
}
