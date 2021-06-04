package wooteco.subway.station.exception;

import wooteco.subway.config.exception.BadRequestException;

public class StationNameDuplicationException extends BadRequestException {
    private static final String ERROR_MESSAGE = "이미 존재하는 역 이름 입니다.";

    public StationNameDuplicationException() {
        super(ERROR_MESSAGE);
    }
}
