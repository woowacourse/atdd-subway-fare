package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class DuplicateException extends SubwayException {

    public DuplicateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
