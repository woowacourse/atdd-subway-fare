package wooteco.subway.line.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "구간이 하나인 노선에서는 구간을 삭제할 수 없습니다.")
public class CannotRemoveSectionException extends RuntimeException {
}
