package wooteco.subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.exception.InvalidEmailException;
import wooteco.subway.auth.exception.InvalidPasswordException;
import wooteco.subway.member.exception.DuplicatedIdException;
import wooteco.subway.station.exception.DuplicatedStationNameException;
import wooteco.subway.station.exception.NoSuchStationException;

@ControllerAdvice
public class controllerAdvice {

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ExceptionResponse> invalidEmailExceptionHandle(InvalidEmailException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        InvalidPasswordException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(DuplicatedIdException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        DuplicatedIdException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(DuplicatedStationNameException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        DuplicatedStationNameException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(NoSuchStationException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        NoSuchStationException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }
}
