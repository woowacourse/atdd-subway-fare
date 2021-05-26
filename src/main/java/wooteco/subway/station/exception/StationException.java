package wooteco.subway.station.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public enum StationException implements SubwayException {

    DUPLICATED_STATION_NAME_EXCEPTION("존재하는 역 이름입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_STATION_NAME_LENGTH("잘못된 역 이름입니다.", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    StationException(String message, int status) {
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
