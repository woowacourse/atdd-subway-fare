package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.auth.InvalidTokenException;
import wooteco.subway.exception.dto.SubwayExceptionResponse;

import java.util.Objects;

@RestControllerAdvice
public class SubwayAdvice {
    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<SubwayExceptionResponse> subwayException(SubwayException e) {
        SubwayExceptionResponse subwayExceptionResponse =
                new SubwayExceptionResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(subwayExceptionResponse);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<SubwayExceptionResponse> invalidTokenException(InvalidTokenException e) {
        SubwayExceptionResponse subwayExceptionResponse =
                new SubwayExceptionResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(subwayExceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SubwayExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        SubwayExceptionResponse subwayExceptionResponse =
                new SubwayExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                        Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(subwayExceptionResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<SubwayExceptionResponse> notFoundException(NotFoundException e) {
        SubwayExceptionResponse subwayExceptionResponse =
                new SubwayExceptionResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(subwayExceptionResponse);
    }
}
