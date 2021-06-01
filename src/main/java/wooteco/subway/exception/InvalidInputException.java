package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends SubwayException {

    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
