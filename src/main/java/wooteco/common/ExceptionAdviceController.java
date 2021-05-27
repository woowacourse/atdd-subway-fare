package wooteco.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.common.exception.badrequest.BadRequestException;
import wooteco.common.exception.notfound.NotFoundException;

@RestControllerAdvice
public class ExceptionAdviceController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity badRequest(BadRequestException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // 여기서는 어떤 에러를 던질 지 고민해봐야됨
    @ExceptionHandler(Exception.class)
    public ResponseEntity serverError(Exception e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
