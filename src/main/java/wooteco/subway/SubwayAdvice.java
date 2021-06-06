package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.SubwayCustomException;

@ControllerAdvice
public class SubwayAdvice {

    private static final String NEW_LINE = System.getProperty("line.separator");

    @ExceptionHandler(SubwayCustomException.class)
    public ResponseEntity<String> subwayException(SubwayCustomException e) {
        return ResponseEntity.status(e.status()).body(e.message());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeException(RuntimeException e) {
        System.out.println(e.getMessage());
        System.out.println(e.getClass());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("알 수 없는 에러가 발생했습니다." + NEW_LINE);
    }
}
