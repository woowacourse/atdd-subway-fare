package wooteco.subway.station.exception;

import wooteco.subway.exception.NotFoundException;

public class StationNotFoundException extends NotFoundException {
    public StationNotFoundException() {
    }

    public StationNotFoundException(String message) {
        super(message);
    }
}
