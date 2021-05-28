package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.exception.dto.ExceptionResponse;

import java.util.Objects;

@RestControllerAdvice
public class SubwayControllerAdvice {
    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ExceptionResponse> subwayException(SubwayException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(e.body());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exception(Exception e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse("서버에서 요청을 처리하지 못했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(Objects.requireNonNull(e.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST.value()));
    }
}
