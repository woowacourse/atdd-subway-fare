package wooteco.subway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ExceptionResponse> exceptionResponse(SubwayException e) {
        LOGGER.error(e.getMessage());

        return ResponseEntity.status(e.status())
            .body(new ExceptionResponse(e.error()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> exceptionResponse(MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage());

        return ResponseEntity.badRequest()
            .body(new ExceptionResponse(e.getMessage()));
    }
}
