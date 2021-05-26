package wooteco.subway.line.ui;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.ErrorResponse;
import wooteco.subway.line.exception.LineDomainException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = LineController.class)
public class LineControllerAdvice {
    @ExceptionHandler(LineDomainException.class)
    public ResponseEntity<ErrorResponse> handleLineDomainException(LineDomainException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}
