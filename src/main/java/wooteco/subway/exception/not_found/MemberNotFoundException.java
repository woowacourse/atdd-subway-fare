package wooteco.subway.exception.not_found;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "존재하지 않는 회원 입니다.")
public class MemberNotFoundException extends RuntimeException{
}
