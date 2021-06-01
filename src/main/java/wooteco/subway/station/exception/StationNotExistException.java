package wooteco.subway.station.exception;

import wooteco.subway.config.exception.BadRequestException;

public class StationNotExistException extends BadRequestException {
    private static final String ERROR_MESSAGE = "존재하지 않는 역입니다.";

    public StationNotExistException() {
        super(ERROR_MESSAGE);
    }
}
