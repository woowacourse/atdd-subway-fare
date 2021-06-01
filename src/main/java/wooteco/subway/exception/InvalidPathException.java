package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidPathException extends SubwayException {

    public InvalidPathException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
