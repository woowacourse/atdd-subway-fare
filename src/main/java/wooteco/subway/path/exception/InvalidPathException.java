package wooteco.subway.path.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPathException extends PathDomainException {

    public InvalidPathException(String message) {
        super(message);
    }
}
