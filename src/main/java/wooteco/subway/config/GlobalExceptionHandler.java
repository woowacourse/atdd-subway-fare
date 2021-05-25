package wooteco.subway.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.path.application.InvalidPathException;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handle(SQLException e) {
        return ResponseEntity
                .badRequest()
                .body("SQLException: " + e.getMessage());
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<String> handle(InvalidPathException e) {
        return ResponseEntity
                .badRequest()
                .body("InvalidPathException: " + e.getMessage());
    }
}
