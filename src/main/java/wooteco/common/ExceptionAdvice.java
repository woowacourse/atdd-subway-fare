package wooteco.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.common.exception.badrequest.BadRequestException;
import wooteco.common.exception.unauthorizationexception.UnAuthorizationException;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UnAuthorizationException.class)
    public ResponseEntity<Object> handleUnAuthorizationException(UnAuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BindingResult> handleValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(e.getBindingResult());
    }
}
