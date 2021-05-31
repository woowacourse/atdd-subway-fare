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

    private static final Logger fileLogger = LoggerFactory.getLogger("file");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> unValidBindingHandler(MethodArgumentNotValidException exception) {
        fileLogger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(bindingResultMessage(exception)));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> authorizeExceptionHandler(final AuthorizationException exception) {
        fileLogger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> InvalidExceptionHandler(InvalidInputException exception) {
        fileLogger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> NotFoundException(NotFoundException exception) {
        fileLogger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ErrorResponse> duplicatedExceptionHandler(DuplicatedException exception) {
        fileLogger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> unsupportedMediaTypeSupportedExceptionHandler(
        final HttpMediaTypeNotSupportedException exception) {
        fileLogger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> serverExceptionHandler(Exception exception) {
        fileLogger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("서버에서 에러가 발생했습니다."));
    }

    private String bindingResultMessage(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    }
}
