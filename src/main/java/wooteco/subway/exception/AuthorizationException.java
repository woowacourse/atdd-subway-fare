package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends RuntimeException {
    private static final String MESSAGE = "인증이 유효하지 않습니다.";

    public AuthorizationException() {
        super(MESSAGE);
    }

    public AuthorizationException(final String message) {
        super(message);
    }
}
