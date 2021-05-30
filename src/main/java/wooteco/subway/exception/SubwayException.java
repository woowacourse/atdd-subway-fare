package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public abstract class SubwayException extends RuntimeException {

    public SubwayException(String message) {
        super(message);
    }

    public abstract HttpStatus status();

    public abstract String error();
}
