package wooteco.common;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

    private HttpStatus status;
    private String message;
    private List<FieldError> errors;

    private ErrorResponse() {
    }

    private ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    private ErrorResponse(HttpStatus status, List<FieldError> errors) {
        this.status = status;
        this.errors = errors;
    }

    public static ErrorResponse of(HttpStatus status, String message) {
        return new ErrorResponse(status, message);
    }

    public static ErrorResponse of(HttpStatus status, BindingResult bindingResult) {
        List<FieldError> errors = new ArrayList<>();
        bindingResult.getFieldErrors()
                .forEach(error -> errors.add(new FieldError(
                        error.getField(),
                        error.getObjectName(),
                        error.getDefaultMessage()))
                );

        return new ErrorResponse(status, errors);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        public FieldError() {
        }

        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public String getField() {
            return field;
        }

        public String getValue() {
            return value;
        }

        public String getReason() {
            return reason;
        }
    }
}
