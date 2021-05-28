package wooteco.subway.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.config.exception.ErrorResponse;
import wooteco.subway.config.exception.HttpException;
import wooteco.subway.path.application.InvalidPathException;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponse> handle(HttpException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(e.getErrorResponse());
    }

    @ExceptionHandler({SQLException.class, InvalidPathException.class})
    public ResponseEntity<ErrorResponse> handle(SQLException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("서버의 오류로 인해 요청에 대한 처리가 실패하였습니다."));
    }
}
