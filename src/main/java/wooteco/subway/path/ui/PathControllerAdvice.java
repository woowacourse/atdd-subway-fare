package wooteco.subway.path.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.ErrorResponse;
import wooteco.subway.path.exception.FareCalculationException;
import wooteco.subway.path.exception.InvalidPathException;
import wooteco.subway.path.exception.PathNotConnectedException;
import wooteco.subway.station.exception.StationNotFoundException;

@RestControllerAdvice(basePackageClasses = PathController.class)
public class PathControllerAdvice {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> MissingServletRequestParameterException() {
        ErrorResponse errorResponse = ErrorResponse.of("알맞지 않은 양식의 경로 조회 요청입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPathException(InvalidPathException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PathNotConnectedException.class)
    public ResponseEntity<ErrorResponse> handlePathNotConnectedException(PathNotConnectedException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStationNotFoundException(StationNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(FareCalculationException.class)
    public ResponseEntity<ErrorResponse> handleFareCalculationException(FareCalculationException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
