package wooteco.subway.station.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayExceptionSetInterface;

public enum StationExceptionSet implements SubwayExceptionSetInterface {

    INVALID_STATION_NAME_EXCEPTION("잘못된 역 이름입니다.", HttpStatus.BAD_REQUEST.value()),
    DUPLICATE_STATION_EXCEPTION("존재하는 역 이름입니다.", HttpStatus.BAD_REQUEST.value()),
    NOT_EXIST_STATION_EXCEPTION("존재하지 않는 역입니다.", HttpStatus.BAD_REQUEST.value()),
    DELETE_USE_STATION_EXCEPTION("노선에 등록된 역은 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    StationExceptionSet(String message, int status) {
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
