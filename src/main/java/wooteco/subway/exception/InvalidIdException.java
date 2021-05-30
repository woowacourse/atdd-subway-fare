package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidIdException extends SubwayException {

    public InvalidIdException() {
        super("유효하지 않은 형식의 ID가 발견되었습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String error() {
        return "INVALID_ID";
    }
}
