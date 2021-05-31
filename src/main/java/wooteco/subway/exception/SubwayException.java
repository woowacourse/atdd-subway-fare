package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.dto.ErrorResponse;

public class SubwayException extends RuntimeException {
    HttpStatus httpStatus;
    ErrorResponse errorResponse;

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
