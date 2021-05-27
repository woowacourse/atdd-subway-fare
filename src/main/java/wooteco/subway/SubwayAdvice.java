package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.SubwayCustomException;

@ControllerAdvice
public class SubwayAdvice {

    @ExceptionHandler(SubwayCustomException.class)
    public ResponseEntity<String> handleSubwayCustomException(SubwayCustomException exception) {
        return ResponseEntity.status(exception.status()).body(exception.message());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        System.out.println(exception.getClass());
        System.out.println(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요");
    }
}
