package wooteco.subway;

import javax.naming.InvalidNameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;


@org.springframework.web.bind.annotation.ControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> validStationNameHandle() {
        return ResponseEntity.badRequest().body(
            new ExceptionResponse(new InvalidNameException("이름에 특수문자는 허용되지 않습니다."))
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> invalidEmailExceptionHandle(Exception e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e));
    }
}
