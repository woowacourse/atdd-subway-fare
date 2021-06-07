package wooteco.subway.infrastructure.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorResponse> handlerSubwayException(SubwayException e) {
        logger.error(e.getErrorMessage());
        return ResponseEntity.status(e.getStatus()).body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error(COMMON_INVALID_INPUT_VALUE.getError() + " - " + e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.of(COMMON_INVALID_INPUT_VALUE, e.getFieldErrors()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(Exception e) {
        logger.error(COMMON_NOT_EXPECTED_ERROR.getError() + " - " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(COMMON_NOT_EXPECTED_ERROR));
    }
}
