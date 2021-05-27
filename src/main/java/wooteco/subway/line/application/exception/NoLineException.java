package wooteco.subway.line.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "존재하지 않는 노선입니다.")
public class NoLineException extends RuntimeException {
    public NoLineException() {
    }
}
