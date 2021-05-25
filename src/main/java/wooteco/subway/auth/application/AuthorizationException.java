package wooteco.subway.auth.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends RuntimeException {
    private static final String message = "인증에 에러가 발생했습니다.";

    public AuthorizationException() {
        super(message);
    }
}
