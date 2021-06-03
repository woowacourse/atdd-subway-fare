package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.dto.ErrorResponse;

public abstract class SubwayException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ErrorResponse errorResponse;

    public SubwayException(HttpStatus httpStatus, ErrorResponse errorResponse) {
        this.httpStatus = httpStatus;
        this.errorResponse = errorResponse;
    }

    public int getHttpStatus() {
        return httpStatus.value();
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
