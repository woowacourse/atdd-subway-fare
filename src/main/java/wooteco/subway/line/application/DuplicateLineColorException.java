package wooteco.subway.line.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateLineColorException extends RuntimeException{
    public DuplicateLineColorException(String message) {
        super(message);
    }
}
