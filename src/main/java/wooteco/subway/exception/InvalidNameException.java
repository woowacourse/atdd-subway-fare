package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidNameException extends SubwayException {

    public InvalidNameException() {
        super("유효한 이름 양식이 아닙니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "INVALID_NAME";
    }
}
