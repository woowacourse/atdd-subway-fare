package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidFareException extends SubwayException {

    public InvalidFareException() {
        super("유효하지 않은 요금입니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "INVALID_FARE";
    }
}
