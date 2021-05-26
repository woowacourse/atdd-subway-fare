package wooteco.subway.line.ui;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.ErrorResponse;
import wooteco.subway.line.exception.LineDaoException;
import wooteco.subway.line.exception.LineDomainException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = LineController.class)
public class LineControllerAdvice {
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("이미 존재하는 노선 이름 혹은 색상입니다."));
    }

    @ExceptionHandler(LineDaoException.class)
    public ResponseEntity<ErrorResponse> handleStationDaoException(LineDaoException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(LineDomainException.class)
    public ResponseEntity<ErrorResponse> handleLineDomainException(LineDomainException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}
