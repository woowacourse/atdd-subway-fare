package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "노선의 전체 글자 수는 2자 이상 10자 이하여야 합니다.")
public class LineInvalidBoundaryException extends RuntimeException {
}
