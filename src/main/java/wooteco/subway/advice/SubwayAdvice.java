package wooteco.subway.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.dto.ErrorResponse;
import wooteco.subway.exception.duplicate.DuplicatedException;

@RestControllerAdvice
public class SubwayAdvice {
    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ErrorResponse> handle(DuplicatedException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(e.getMessage()));
    }
}
