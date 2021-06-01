package wooteco.subway.config.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.config.ErrorMessage;

public class AuthorizationException extends BusinessException {
    public AuthorizationException(String message) {
        super(HttpStatus.UNAUTHORIZED, new ErrorMessage(message));
    }
}
