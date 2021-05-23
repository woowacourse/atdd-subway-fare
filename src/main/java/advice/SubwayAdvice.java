package advice;

import exception.dto.ErrorResponse;
import exception.duplicate.DuplicatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SubwayAdvice {
    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ErrorResponse> duplicate(Exception e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(e.getMessage()));
    }
}
