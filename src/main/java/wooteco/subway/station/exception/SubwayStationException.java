package wooteco.subway.station.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public enum SubwayStationException implements SubwayException {

    INVALID_STATION_NAME_EXCEPTION("잘못된 역 이름입니다.", HttpStatus.BAD_REQUEST.value()),
    DUPLICATE_STATION_EXCEPTION("존재하는 역 이름입니다.", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    SubwayStationException(String message, int status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public int status() {
        return status;
    }
}
