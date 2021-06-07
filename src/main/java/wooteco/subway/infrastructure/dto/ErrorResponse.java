package wooteco.subway.infrastructure.dto;

import org.springframework.validation.FieldError;
import wooteco.subway.infrastructure.ErrorCode;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String message;
    private final List<ErrorDto> errors;

    private ErrorResponse(final int status, final String message, final List<ErrorDto> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getError(), Collections.emptyList());
    }

    public static ErrorResponse of(ErrorCode errorCode, List<FieldError> fieldErrors) {
        return new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getError(), ErrorDto.listOf(fieldErrors));
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public List<ErrorDto> getErrors() {
        return errors;
    }

    public String getMessage() {
        return message;
    }

}
