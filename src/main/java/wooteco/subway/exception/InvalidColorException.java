package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidColorException extends SubwayException {

    public InvalidColorException() {
        super("유효하지 않은 노선 색상입니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "INVALID_COLOR";
    }
}
