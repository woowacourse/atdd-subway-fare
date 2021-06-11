package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends SubwayException {

    public InvalidPasswordException() {
        super("유효하지 않은 비밀번호 입니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "INVALID_PASSWORD";
    }
}
