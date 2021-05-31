package wooteco.subway.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "이메일 전체 글자 수는 30글자를 넘을 수 없습니다.")
public class EmailInvalidBoundaryException extends RuntimeException {
}
