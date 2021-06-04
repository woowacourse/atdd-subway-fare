package wooteco.subway;

import java.nio.file.InvalidPathException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.dto.ErrorResponse;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.exception.ValidationFailureException;
import wooteco.subway.exception.WrongNameConventionException;
import wooteco.subway.line.exception.LineCompositionException;
import wooteco.subway.line.exception.LineRemovalException;
import wooteco.subway.member.exception.DuplicatedEmailAddressException;
import wooteco.subway.station.exception.ExistedStationOnPathException;

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

    @ExceptionHandler(DuplicatedEmailAddressException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotFoundException(
        DuplicatedEmailAddressException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ValidationFailureException.class)
    public ResponseEntity<ErrorResponse> handleValidationFailureException(
        ValidationFailureException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ExistedStationOnPathException.class)
    public ResponseEntity<ErrorResponse> handleExistedStationOnPathException(
        ExistedStationOnPathException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(WrongNameConventionException.class)
    public ResponseEntity<ErrorResponse> handleWrongNameConventionException(
        WrongNameConventionException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage()));
    }
}
