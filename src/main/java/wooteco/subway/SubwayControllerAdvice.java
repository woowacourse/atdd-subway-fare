package wooteco.subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.exception.DuplicatedEmailException;
import wooteco.subway.exception.ExceptionResponse;

@ControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandle(DuplicatedEmailException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}
