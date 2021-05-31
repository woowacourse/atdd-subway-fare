package wooteco.subway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger("file");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleBindingException(MethodArgumentNotValidException methodArgumentNotValidException) {
        String message = methodArgumentNotValidException.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining(System.lineSeparator()));
        return ResponseEntity.badRequest()
                .body(message);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException businessException) {
        LOGGER.info(businessException.getMessage());
        return ResponseEntity.status(businessException.getStatus())
                .body(businessException.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSqlException(SQLException sqlException) {
        LOGGER.error(sqlException.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
