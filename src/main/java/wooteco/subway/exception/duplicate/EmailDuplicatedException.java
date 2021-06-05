package wooteco.subway.exception.duplicate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "이메일이 중복되었습니다.")
public class EmailDuplicatedException extends RuntimeException {
    public EmailDuplicatedException() {
    }
}
