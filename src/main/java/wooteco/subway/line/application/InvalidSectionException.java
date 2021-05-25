package wooteco.subway.line.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "구간 입력이 잘못되었습니다.")
public class InvalidSectionException extends RuntimeException {
    public InvalidSectionException() {
    }
}
