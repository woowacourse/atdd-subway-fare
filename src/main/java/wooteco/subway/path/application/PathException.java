package wooteco.subway.path.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PathException extends RuntimeException {
    public PathException(String message) {
        super(message);
    }
}
