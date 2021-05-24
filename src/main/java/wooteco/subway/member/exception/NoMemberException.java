package wooteco.subway.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "이메일과 비밀번호를 확인해주세요.")
public class NoMemberException extends RuntimeException {
    public NoMemberException() {
    }
}
