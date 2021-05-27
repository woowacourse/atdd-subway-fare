package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.CannotRemoveStationException;
import wooteco.subway.exception.DuplicatedNameException;
import wooteco.subway.exception.InvalidPathException;
import wooteco.subway.exception.NotFoundException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(DuplicatedNameException.class)
    public ResponseEntity<String> handleDuplicatedStationNameException(
        DuplicatedNameException e
    ) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<String> handleInvalidPathException(InvalidPathException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(CannotRemoveStationException.class)
    public ResponseEntity<String> handleCannotRemoveStationException(
        CannotRemoveStationException e
    ) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
