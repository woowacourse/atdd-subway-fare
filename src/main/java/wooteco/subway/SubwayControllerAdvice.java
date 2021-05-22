package wooteco.subway;

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
}
