package wooteco.subway.infrastructure.exception.duplicate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "중복된 지하철 노선입니다")
public class LineDuplicatedException extends RuntimeException {
    public LineDuplicatedException() {
    }
}
