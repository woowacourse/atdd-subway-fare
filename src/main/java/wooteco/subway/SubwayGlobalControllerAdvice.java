package wooteco.subway;

import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.auth.exception.AuthorizationException;

@RestControllerAdvice
public class SubwayGlobalControllerAdvice {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Void> handleAuthException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handleRuntimeException() {
        return ResponseEntity.badRequest()
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleUnexpectedException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
