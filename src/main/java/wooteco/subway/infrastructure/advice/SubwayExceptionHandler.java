package wooteco.subway.infrastructure.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.infrastructure.dto.ErrorResponse;
import wooteco.subway.infrastructure.exception.domain.SubwayException;

import static wooteco.subway.infrastructure.ErrorCode.COMMON_INVALID_INPUT_VALUE;
import static wooteco.subway.infrastructure.ErrorCode.COMMON_NOT_EXPECTED_ERROR;

@RestControllerAdvice
public class SubwayExceptionHandler {

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorResponse> handlerSubwayException(SubwayException e) {
        return ResponseEntity.status(e.getStatus()).body(ErrorResponse.of(e.getErrorCode()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(COMMON_INVALID_INPUT_VALUE, e.getFieldErrors()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleApplicationException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(COMMON_NOT_EXPECTED_ERROR));
    }
}
