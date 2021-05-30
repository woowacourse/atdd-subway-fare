package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidAgeException extends SubwayException {

    public InvalidAgeException() {
        super("나이 입력 값이 올바르지 않습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "INVALID_AGE";
    }
}
