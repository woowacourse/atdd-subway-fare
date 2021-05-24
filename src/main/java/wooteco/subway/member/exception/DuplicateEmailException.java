package wooteco.subway.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "이메일이 중복되었습니다.")
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
    }
}
