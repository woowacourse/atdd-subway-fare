package wooteco.subway.line.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "중복된 지하철 노선입니다.")
public class DuplicateLineException extends RuntimeException {
    public DuplicateLineException() {
    }
}
