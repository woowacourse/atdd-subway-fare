package wooteco.subway.config.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.config.ErrorMessage;

public class BusinessException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ErrorMessage errorMessage;

    public BusinessException(HttpStatus httpStatus, String errorMessage) {
        this(httpStatus, new ErrorMessage(errorMessage));
    }

    public BusinessException(HttpStatus httpStatus, ErrorMessage errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
