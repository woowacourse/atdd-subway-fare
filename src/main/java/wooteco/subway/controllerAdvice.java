package wooteco.subway;

import javax.naming.InvalidNameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.exception.InvalidEmailException;
import wooteco.subway.auth.exception.InvalidPasswordException;
import wooteco.subway.auth.exception.InvalidTokenException;
import wooteco.subway.line.exception.BothStationAlreadyRegisteredInLineException;
import wooteco.subway.line.exception.BothStationNotRegisteredInLineException;
import wooteco.subway.line.exception.DuplicatedLineNameException;
import wooteco.subway.line.exception.ImpossibleDistanceException;
import wooteco.subway.line.exception.InvalidDistanceException;
import wooteco.subway.line.exception.NoSuchLineException;
import wooteco.subway.line.exception.SameStationsInSameSectionException;
import wooteco.subway.member.exception.DuplicatedIdException;
import wooteco.subway.station.exception.DuplicatedStationNameException;
import wooteco.subway.station.exception.NoSuchStationException;
import wooteco.subway.station.exception.StationAlreadyRegisteredInLineException;


@ControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @ExceptionHandler(StationAlreadyRegisteredInLineException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        StationAlreadyRegisteredInLineException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        InvalidTokenException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(DuplicatedLineNameException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        DuplicatedLineNameException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> validStationNameHandle() {
        return ResponseEntity.badRequest().body(
            new ExceptionResponse(new InvalidNameException("이름에 특수문자는 허용되지 않습니다."))
        );
    }

    @ExceptionHandler(NoSuchLineException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        NoSuchLineException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(BothStationNotRegisteredInLineException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        BothStationNotRegisteredInLineException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(BothStationAlreadyRegisteredInLineException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        BothStationAlreadyRegisteredInLineException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(SameStationsInSameSectionException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        SameStationsInSameSectionException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(InvalidDistanceException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        InvalidDistanceException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }

    @ExceptionHandler(ImpossibleDistanceException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionHandle(
        ImpossibleDistanceException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }
}
