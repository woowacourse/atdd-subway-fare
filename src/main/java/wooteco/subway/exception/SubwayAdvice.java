package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.auth.InvalidTokenException;
import wooteco.subway.exception.dto.SubwayExceptionResponse;

@RestControllerAdvice
public class SubwayAdvice {
    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<SubwayExceptionResponse> subwayException(SubwayException e) {
        SubwayExceptionResponse subwayExceptionResponse =
                new SubwayExceptionResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(subwayExceptionResponse);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<SubwayExceptionResponse> invalidTokenException(InvalidTokenException e) {
        SubwayExceptionResponse subwayExceptionResponse =
                new SubwayExceptionResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(subwayExceptionResponse);
    }
}
