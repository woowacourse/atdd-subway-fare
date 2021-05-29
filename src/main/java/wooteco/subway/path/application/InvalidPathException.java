package wooteco.subway.path.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import wooteco.subway.exception.InvalidRequestException;

public class InvalidPathException extends InvalidRequestException {

    public InvalidPathException() {
    }

    public InvalidPathException(String message) {
        super(message);
    }
}
