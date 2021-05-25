package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends RuntimeException {

    private static final String MESSAGE = "이메일 혹은 비밀번호를 다시 확인해주세요.";

    public AuthorizationException() {
        super(MESSAGE);
    }
}
