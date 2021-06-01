package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity handleDuplicatedEmailException(DuplicatedException e) {
        String message = e.getMessage();
        ExceptionResponse exceptionRes = new ExceptionResponse(message, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(exceptionRes);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        ExceptionResponse exceptionRes = new ExceptionResponse(msg, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(exceptionRes);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity handleIllegalStateException(IllegalStateException e) {
        ExceptionResponse exceptionRes = new ExceptionResponse(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity(exceptionRes, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException e) {
        ExceptionResponse exceptionRes = new ExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity(exceptionRes, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidInsertException.class)
    public ResponseEntity handleInvalidInsertException(InvalidInsertException e) {
        ExceptionResponse exceptionRes = new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(exceptionRes);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity handleAuthorizationException(AuthorizationException e) {
        ExceptionResponse exceptionRes = new ExceptionResponse(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity(exceptionRes, HttpStatus.UNAUTHORIZED);
    }

}
