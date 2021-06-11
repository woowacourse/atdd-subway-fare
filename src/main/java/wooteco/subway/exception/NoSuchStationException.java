package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class NoSuchStationException extends SubwayException {

    public NoSuchStationException() {
        super("입력된 지하철 역을 찾을 수 없습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "NO_SUCH_STATION";
    }
}
