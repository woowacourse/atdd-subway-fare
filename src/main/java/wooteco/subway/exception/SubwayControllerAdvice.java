package wooteco.subway.exception;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandle(SubwayException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler()
    public ResponseEntity<ExceptionResponse> invalidTokenExceptionHandle(AuthorizationException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> validExceptionHandle(MethodArgumentNotValidException e) {
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        SubwayExceptions subwayExceptions = SubwayExceptions.valueOf(message);
        ExceptionResponse exceptionResponse = new ExceptionResponse(subwayExceptions.makeException());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
