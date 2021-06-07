package wooteco.subway.infrastructure.exception.domain;

import org.springframework.http.HttpStatus;
import wooteco.subway.infrastructure.ErrorCode;

public class SubwayException extends RuntimeException {
    private final ErrorCode errorCode;

    public SubwayException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return errorCode.getHttpStatus();
    }

    public String getErrorMessage() {
        return errorCode.getError();
    }
}
