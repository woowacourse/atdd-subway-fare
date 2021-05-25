package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotLoginException extends RuntimeException {

    private static final String MESSAGE = "로그인을 해야 합니다.";

    public NotLoginException() {
        super(MESSAGE);
    }
}
