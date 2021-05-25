package wooteco.subway.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.ErrorMessage;
import wooteco.subway.exception.HttpException;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorMessage> resolveRequest(Exception e) {
        return ResponseEntity
            .badRequest()
            .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorMessage> handleException(HttpException e) {
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(e.getErrorMessage());
    }
}
