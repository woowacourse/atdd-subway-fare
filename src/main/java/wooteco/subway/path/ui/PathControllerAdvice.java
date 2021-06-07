package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.dto.ErrorResponse;
import wooteco.subway.line.exception.LineDaoException;
import wooteco.subway.line.exception.LineDomainException;
import wooteco.subway.station.exception.StationDaoException;

@RestControllerAdvice(assignableTypes = PathController.class)
public class PathControllerAdvice {

    @ExceptionHandler(value={LineDaoException.class, LineDomainException.class})
    public ResponseEntity<ErrorResponse> handleLineException(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value={StationDaoException.class})
    public ResponseEntity<ErrorResponse> handleStationException(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}
