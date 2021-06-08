package wooteco.subway.line.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDistanceException extends RuntimeException {

    public InvalidDistanceException(String message) {
        super(message);
    }
}
