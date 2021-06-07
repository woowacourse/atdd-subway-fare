package wooteco.subway.exception;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.auth.InvalidTokenException;
import wooteco.subway.exception.dto.SubwayExceptionResponse;

import java.util.Objects;

@RestControllerAdvice
public class SubwayAdvice {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<SubwayExceptionResponse> subwayException(SubwayException e) {
        int httpStatus = HttpStatus.BAD_REQUEST.value();
        SubwayExceptionResponse subwayExceptionResponse =
                new SubwayExceptionResponse(httpStatus, e.getMessage());
        logger.error("상태 코드: {}, 에러 메시지: {}", httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus).body(subwayExceptionResponse);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<SubwayExceptionResponse> invalidTokenException(InvalidTokenException e) {
        int httpStatus = HttpStatus.UNAUTHORIZED.value();
        SubwayExceptionResponse subwayExceptionResponse =
                new SubwayExceptionResponse(httpStatus, e.getMessage());
        logger.error("상태 코드: {}, 에러 메시지: {}", httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus).body(subwayExceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SubwayExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        int httpStatus = HttpStatus.BAD_REQUEST.value();
        SubwayExceptionResponse subwayExceptionResponse =
                new SubwayExceptionResponse(httpStatus, Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
        logger.error("상태 코드: {}, 에러 메시지: {}", httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus).body(subwayExceptionResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<SubwayExceptionResponse> notFoundException(NotFoundException e) {
        int httpStatus = HttpStatus.NOT_FOUND.value();
        SubwayExceptionResponse subwayExceptionResponse =
                new SubwayExceptionResponse(httpStatus, e.getMessage());
        logger.error("상태 코드: {}, 에러 메시지: {}", httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus).body(subwayExceptionResponse);
    }
}
