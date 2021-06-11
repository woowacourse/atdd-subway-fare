package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidPathException extends SubwayException {

    public InvalidPathException() {
        super("유효하지 않은 최단거리 요청입니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "INVALID_PATH";
    }
}
