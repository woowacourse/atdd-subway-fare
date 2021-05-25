package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDeleteSectionException extends RuntimeException {

    public InvalidDeleteSectionException(String message) {
        super(message);
    }
}