package wooteco.subway.station.exception;

import wooteco.subway.exception.ErrorCode;
import wooteco.subway.exception.NotFoundException;

public class StationNotFoundException extends NotFoundException {

    public StationNotFoundException() {
        super(ErrorCode.NOTFOUND_STATION);
    }
}
