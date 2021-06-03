package wooteco.subway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.SubwayException;

@RestControllerAdvice
public class SubwayAdvice {

    private static final Logger log = LoggerFactory.getLogger("console");

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<String> handleSubwayCustomException(SubwayException exception) {
        return ResponseEntity.status(exception.status()).body(exception.message());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        log.error(exception.getClass() + ": " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요");
    }
}
