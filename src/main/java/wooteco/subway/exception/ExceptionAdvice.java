package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({DuplicatedException.class, InvalidInsertException.class})
    public ResponseEntity handleDuplicatedEmailException(RuntimeException e) {
        ExceptionResponse exceptionRes = new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(exceptionRes);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        ExceptionResponse exceptionRes = new ExceptionResponse(msg, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(exceptionRes);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException e) {
        ExceptionResponse exceptionRes = new ExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity(exceptionRes, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IllegalStateException.class, AuthorizationException.class})
    public ResponseEntity handleIllegalStateException(RuntimeException e) {
        ExceptionResponse exceptionRes = new ExceptionResponse(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity(exceptionRes, HttpStatus.UNAUTHORIZED);
    }
}
