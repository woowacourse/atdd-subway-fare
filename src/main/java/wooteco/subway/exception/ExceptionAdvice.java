package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity handleDuplicatedEmailException(DuplicatedEmailException e) {
        String message = e.getMessage();
        ExceptionResponse exceptionRes = new ExceptionResponse(message, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(exceptionRes);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ExceptionResponse exceptionRes = new ExceptionResponse(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(exceptionRes);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        ExceptionResponse exceptionRes = new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(exceptionRes);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException e) {
        ExceptionResponse exceptionRes = new ExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity(exceptionRes, HttpStatus.NOT_FOUND);
    }

}
