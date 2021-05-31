package wooteco.subway.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "비밀번호는 4글자 이상 20글자 이하이어야 합니다.")
public class PasswordInvalidBoundaryException extends RuntimeException {
}
