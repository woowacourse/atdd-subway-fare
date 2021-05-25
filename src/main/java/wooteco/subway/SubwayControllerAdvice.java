package wooteco.subway;

import java.nio.file.InvalidPathException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.dto.ErrorResponse;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.line.exception.LineCompositionException;
import wooteco.subway.line.exception.LineRemovalException;
import wooteco.subway.member.exception.EmailAddressNotFoundException;

@RestControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler({LineCompositionException.class, LineRemovalException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<ErrorResponse> handleInValidPatahException(InvalidPathException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(EmailAddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotFoundException(EmailAddressNotFoundException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(e.getMessage()));
    }
}
