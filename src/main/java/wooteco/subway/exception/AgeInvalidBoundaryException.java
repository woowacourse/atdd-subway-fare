package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "나이는 음수일 수 없으며 200살을 넘을 수 없습니다.")
public class AgeInvalidBoundaryException extends RuntimeException {
}
