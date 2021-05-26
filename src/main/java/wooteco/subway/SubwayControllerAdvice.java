package wooteco.subway;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.exception.DuplicatedEmailException;
import wooteco.subway.exception.ExceptionResponse;
import wooteco.subway.exception.InvalidInputException;

@ControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandle(DuplicatedEmailException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> validExceptionHandle(MethodArgumentNotValidException e) {
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        ExceptionResponse exceptionResponse = new ExceptionResponse(new InvalidInputException(message));
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}
