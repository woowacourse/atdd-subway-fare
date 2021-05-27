package wooteco.subway.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.exception.InvalidInputException;
import wooteco.subway.exception.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> unValidBindingHandler(MethodArgumentNotValidException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(bindingResultMessage(exception)));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> authorizeExceptionHandler(final AuthorizationException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> InvalidExceptionHandler(InvalidInputException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> NotFoundException(NotFoundException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ErrorResponse> duplicatedExceptionHandler(DuplicatedException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> unsupportedMediaTypeSupportedExceptionHandler(
        final HttpMediaTypeNotSupportedException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> serverExceptionHandler(RuntimeException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(exception.getMessage()));
    }

    private String bindingResultMessage(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    }
}
