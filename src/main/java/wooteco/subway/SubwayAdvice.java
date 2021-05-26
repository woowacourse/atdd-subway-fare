package wooteco.subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.SubwayCustomException;

@ControllerAdvice
public class SubwayAdvice {

    @ExceptionHandler(SubwayCustomException.class)
    public ResponseEntity<String> subwayException(SubwayCustomException e) {
        return ResponseEntity.status(e.status()).body(e.message());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body("알 수 없는 에러가 발생했습니다.");
    }
}
