package wooteco.subway.infrastructure.advice;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import wooteco.subway.infrastructure.dto.ErrorResponse;

import static wooteco.subway.infrastructure.ErrorCode.INVALID_INPUT_VALUE;
import static wooteco.subway.infrastructure.ErrorCode.NOT_EXPECTED_ERROR;

@RestControllerAdvice
public class SubwayExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(Exception e) throws Exception {
        System.out.println("error: " + e.getMessage());
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(NOT_EXPECTED_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(INVALID_INPUT_VALUE, e.getFieldErrors()));
    }
}
