package wooteco.subway.station.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.ErrorResponse;
import wooteco.subway.station.exception.DuplicateStationNameException;
import wooteco.subway.station.exception.InvalidStationNameException;
import wooteco.subway.station.exception.StationInLineException;
import wooteco.subway.station.exception.StationNotFoundException;

import java.util.Objects;

@RestControllerAdvice(basePackageClasses = StationController.class)
public class StationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String defaultMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        ErrorResponse errorResponse = ErrorResponse.of(defaultMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicateStationNameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateStationNameException(DuplicateStationNameException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStationNotFoundException(StationNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidStationNameException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStationNameException(InvalidStationNameException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(StationInLineException.class)
    public ResponseEntity<ErrorResponse> handleStationInLineException(StationInLineException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
