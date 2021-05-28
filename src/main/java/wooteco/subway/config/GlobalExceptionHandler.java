package wooteco.subway.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.config.exception.ErrorResponse;
import wooteco.subway.config.exception.HttpException;
import wooteco.subway.path.application.InvalidPathException;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponse> handle(HttpException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(e.getErrorResponse());
    }

    @ExceptionHandler({SQLException.class, InvalidPathException.class})
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e));
    }
}
