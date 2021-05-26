package wooteco.subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.exception.InvalidEmailException;
import wooteco.subway.auth.exception.InvalidPasswordException;

@ControllerAdvice
public class controllerAdvice {

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ExceptionResponse> invalidEmailExceptionHandle(InvalidEmailException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        InvalidPasswordException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }
}
