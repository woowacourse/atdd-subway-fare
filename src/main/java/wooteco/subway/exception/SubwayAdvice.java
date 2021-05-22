package wooteco.subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SubwayAdvice {
    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<SubwayExceptionResponse> subwayException(SubwayException e) {
        SubwayExceptionResponse subwayExceptionResponse =
                new SubwayExceptionResponse(e.getHttpStatus(), e.getMessage());
        return ResponseEntity.ok(subwayExceptionResponse);
    }
}
