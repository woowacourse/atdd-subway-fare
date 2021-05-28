package wooteco.subway.station.ui;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.ErrorResponse;
import wooteco.subway.station.exception.StationDaoException;

import java.sql.SQLIntegrityConstraintViolationException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = StationController.class)
public class StationControllerAdvice {
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("이미 존재하는 역 이름입니다."));
    }

    @ExceptionHandler(StationDaoException.class)
    public ResponseEntity<ErrorResponse> handleStationDaoException(StationDaoException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("노선에 등록된 역은 삭제할 수 없습니다."));
    }

}
