package wooteco.subway.line.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.ErrorResponse;
import wooteco.subway.line.exception.*;
import wooteco.subway.station.exception.StationNotFoundException;

import java.util.Objects;

@RestControllerAdvice(basePackageClasses = LineController.class)
public class LineControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String defaultMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        ErrorResponse errorResponse = ErrorResponse.of(defaultMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({LineNotFoundException.class, StationNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleLineNotFoundException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({DuplicateLineNameException.class, DuplicateLineColorException.class})
    public ResponseEntity<ErrorResponse> handleLDuplicateException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler({InvalidLineNameException.class, InvalidLineColorException.class})
    public ResponseEntity<ErrorResponse> handleInvalidFieldException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({SectionRegisterException.class, SectionRemovalException.class})
    public ResponseEntity<ErrorResponse> handleSectionException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
