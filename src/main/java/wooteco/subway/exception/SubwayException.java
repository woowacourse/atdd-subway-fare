package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class SubwayException extends RuntimeException {
    private final int httpStatus;
    private final String message;

    public SubwayException(String message) {
        this.httpStatus = HttpStatus.BAD_REQUEST.value();
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
