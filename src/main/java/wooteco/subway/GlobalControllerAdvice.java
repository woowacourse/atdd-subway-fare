package wooteco.subway;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.exception.ErrorResponse;

import java.sql.SQLException;

@Order
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> resolveRequest(BindException e) {
        e.printStackTrace();
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("예상치 못한 DB 예외가 발생했습니다." + System.getProperty("line.separator") + "에러 메세지: " + e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("예상치 못한 예외가 발생했습니다." + System.getProperty("line.separator") + "에러 메세지: " + e.getMessage()));
    }
}
