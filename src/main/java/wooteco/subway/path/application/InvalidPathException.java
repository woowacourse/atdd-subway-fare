package wooteco.subway.path.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidPathException extends RuntimeException {
    public InvalidPathException() {
    }

    public InvalidPathException(String message) {
        super(message);
    }
}
