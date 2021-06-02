package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class StationAlreadyRegisteredInLineException extends SubwayException {

    public StationAlreadyRegisteredInLineException() {
        super("지하철 노선에 구간으로 등록되어 있는 역은 삭제할 수 없습니다. ");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "STATION_ALREADY_REGISTERED_IN_LINE";
    }
}
