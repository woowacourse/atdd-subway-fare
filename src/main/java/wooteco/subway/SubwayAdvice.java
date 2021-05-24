package wooteco.subway;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.exception.SubwayException;

@RestControllerAdvice(annotations = RestController.class)
public class SubwayAdvice {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<MessageDto> handleSQLException() {
        return ResponseEntity.badRequest().body(new MessageDto("데이터베이스 에러"));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<MessageDto> handleAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<MessageDto> handleSubwayException(SubwayException e) {
        return ResponseEntity.badRequest().body(new MessageDto(e.getMessage()));
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
