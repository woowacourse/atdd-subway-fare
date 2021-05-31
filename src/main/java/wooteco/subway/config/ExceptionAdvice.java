package wooteco.subway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.SubwayException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionAdvice {
    private final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleServerErrorException(Exception e) {
        logger.error("internal server error has been occurred. message=[{}]", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("관리자에게 문의해주세요."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleBindException(MethodArgumentNotValidException e) {
        logger.error("method argument not valid exception occurred. message=[{}]", e.getMessage(), e);

        String message = e.getBindingResult().getAllErrors().stream()
                .filter(err -> err != null && err.getCodes() != null)
                .filter(err -> err.getCodes().length > 1)
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(message));
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(SubwayException e) {
        logger.error("Runtime Exception occurred, check user's input. message=[{}]", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorMessage> handleAuthorizationException(AuthorizationException e) {
        logger.error("Exception occurred regards to authorization. message=[{}]", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage(e.getMessage()));
    }
}
