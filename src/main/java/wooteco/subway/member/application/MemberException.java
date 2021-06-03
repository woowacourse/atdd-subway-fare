package wooteco.subway.member.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MemberException extends RuntimeException {

    public MemberException() {
        super("이미 사용되고 있는 이메일입니다.");
    }
}
