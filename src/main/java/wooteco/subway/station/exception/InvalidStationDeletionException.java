package wooteco.subway.station.exception;

import wooteco.subway.exception.BusinessException;
import wooteco.subway.exception.ErrorCode;

public class InvalidStationDeletionException extends BusinessException {

    public InvalidStationDeletionException() {
        super(ErrorCode.INVALID_STATION_DELETION);
    }
}
