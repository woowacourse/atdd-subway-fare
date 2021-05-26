package wooteco.subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.ExceptionResponse;
import wooteco.subway.auth.exception.InvalidEmailException;

@ControllerAdvice
public class controllerAdvice {

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ExceptionResponse> invalidEmailExceptionHandle(InvalidEmailException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }
}
