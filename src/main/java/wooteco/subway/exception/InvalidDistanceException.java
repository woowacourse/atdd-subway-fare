package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidDistanceException extends SubwayException {

    public InvalidDistanceException() {
        super("올바르지 않은 구간 거리 입력입니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "INVALID_DISTANCE";
    }
}
