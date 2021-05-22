package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.exception.dto.ExceptionResponse;

@RestControllerAdvice
public class SubwayControllerAdvice {
    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ExceptionResponse> subwayException(SubwayException subwayException) {
        return ResponseEntity
                .status(subwayException.getHttpStatus())
                .body(new ExceptionResponse(subwayException.getMessage(), subwayException.getHttpStatus().value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exception(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
