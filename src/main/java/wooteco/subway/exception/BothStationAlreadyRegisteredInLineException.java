package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class BothStationAlreadyRegisteredInLineException extends SubwayException {

    public BothStationAlreadyRegisteredInLineException() {
        super("이미 노선에 구간으로 등록된 역들 입니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "BOTH_STATION_ALREADY_REGISTERED_IN_LINE";
    }
}
