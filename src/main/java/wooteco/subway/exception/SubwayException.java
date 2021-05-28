package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.dto.ExceptionResponse;

public class SubwayException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ExceptionResponse body;

    public SubwayException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.body = new ExceptionResponse(message, httpStatus.value());
    }

    public SubwayException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.body = new ExceptionResponse(message, httpStatus.value());
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ExceptionResponse body() {
        return this.body;
    }
}
