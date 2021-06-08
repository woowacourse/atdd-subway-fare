package wooteco.subway.dto;

import lombok.Getter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class BindErrorResponse extends ErrorResponse {
    private final Map<String, String> causes;

    public BindErrorResponse(String message, Map<String, String> causes) {
        super(message);
        this.causes = causes;
    }

    public static BindErrorResponse from(String message, BindingResult bindingResult) {
        Map<String, String> causes = bindingResult.getAllErrors().stream()
                .collect(Collectors.toMap(error -> ((FieldError) error).getField(), DefaultMessageSourceResolvable::getDefaultMessage));

        return new BindErrorResponse(message, causes);
    }
}
