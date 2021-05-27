package wooteco.subway.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.addition.CannotAddException;
import wooteco.subway.exception.auth.AuthorizationException;
import wooteco.subway.exception.deletion.CannotDeleteException;
import wooteco.subway.exception.duplication.DuplicatedException;
import wooteco.subway.exception.notfound.NotFoundException;
import wooteco.subway.exception.path.InvalidPathException;

import java.sql.SQLException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleBindingException(MethodArgumentNotValidException methodArgumentNotValidException) {
        String message = methodArgumentNotValidException.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));
        return ResponseEntity.badRequest()
                .body(message);
    }

    @ExceptionHandler({CannotAddException.class, CannotDeleteException.class, DuplicatedException.class, InvalidPathException.class})
    public ResponseEntity<String> handleBadRequest(RuntimeException runtimeException) {
        return ResponseEntity.badRequest()
                .body(runtimeException.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(RuntimeException runtimeException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(runtimeException.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleUnauthorized(RuntimeException runtimeException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(runtimeException.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSqlException(SQLException sqlException) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
