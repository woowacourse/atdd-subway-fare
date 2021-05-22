package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    
    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity handleDuplicatedEmailException(DuplicatedEmailException e) {
        String message = e.getMessage();
        ExceptionResponse exceptionResponse = new ExceptionResponse(message, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }


}
