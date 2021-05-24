package wooteco.subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.DuplicatedStationNameException;
import wooteco.subway.exception.NotFoundException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DuplicatedStationNameException.class)
    public ResponseEntity<String> handleDuplicatedStationNameException(
        DuplicatedStationNameException e
    ) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
