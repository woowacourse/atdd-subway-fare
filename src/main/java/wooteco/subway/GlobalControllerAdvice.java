package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.station.application.StationException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorizationException(AuthorizationException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
    }

    @ExceptionHandler(StationException.class)
    public ResponseEntity<ExceptionResponse> handleStationException(StationException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}
