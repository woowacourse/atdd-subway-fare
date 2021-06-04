package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public abstract class SubwayException extends RuntimeException {
    private HttpStatus status;

    public SubwayException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
