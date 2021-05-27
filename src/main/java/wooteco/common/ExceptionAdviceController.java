package wooteco.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.common.exception.badrequest.BadRequestException;
import wooteco.common.exception.forbidden.AuthorizationException;
import wooteco.common.exception.notfound.NotFoundException;

@RestControllerAdvice
public class ExceptionAdviceController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> badRequest(BadRequestException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> forbidden(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BindingResult> argumentNotValid(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(e.getBindingResult());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> serverError(Exception e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
