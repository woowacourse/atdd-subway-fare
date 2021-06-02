package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class BothStationNotRegisteredInLineException extends SubwayException {

    public BothStationNotRegisteredInLineException() {
        super("해당 노선에 구간으로 등록 되어있는 역이 없습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "BOTH_STATION_NOT_REGISTERED_IN_LINE";
    }
}
