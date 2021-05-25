package wooteco.subway.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.subway.exception.dto.ErrorResponse;

@RestControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("처리하지 않은 서버에러입니다 ㅠㅜ.."));
    }

    @ExceptionHandler(SectionException.class)
    public ResponseEntity<ErrorResponse> handleSectionException(SectionException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getCause().getMessage()));
    }
}
