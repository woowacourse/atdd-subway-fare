package wooteco.subway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.exception.SubwayException;

import java.util.Objects;

@RestControllerAdvice(annotations = RestController.class)
public class SubwayAdvice {
    private static Logger logger = LoggerFactory.getLogger(SubwayAdvice.class);

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<MessageDto> handleSQLException(DataAccessException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.badRequest().body(new MessageDto("데이터베이스 에러"));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageDto> handleNotFoundException(NotFoundException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.badRequest().body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<MessageDto> handleAuthorizationException(AuthorizationException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<MessageDto> handleSubwayException(SubwayException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.badRequest().body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<MessageDto> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.badRequest().body(new MessageDto(
            Objects.requireNonNull(e.getBindingResult()
                    .getFieldError())
                .getField()
            )
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageDto> handleRuntimeException(RuntimeException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public static class MessageDto {

        private final String message;

        public MessageDto(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

    }

}
