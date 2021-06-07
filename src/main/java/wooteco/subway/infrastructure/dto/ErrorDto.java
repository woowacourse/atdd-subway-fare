package wooteco.subway.infrastructure.dto;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class ErrorDto {
    private final String field;
    private final String reason;

    public ErrorDto(final String field, final String reason) {
        this.field = field;
        this.reason = reason;
    }

    public static List<ErrorDto> listOf(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(error -> new ErrorDto(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    public String getField() {
        return field;
    }

    public String getReason() {
        return reason;
    }
}
