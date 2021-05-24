package wooteco.subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.common.exception.notfound.NotFoundException;

@ControllerAdvice
public class ExceptionAdviceController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFound(NotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
