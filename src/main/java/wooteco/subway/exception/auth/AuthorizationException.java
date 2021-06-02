package wooteco.subway.exception.auth;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.CustomException;

public class AuthorizationException extends CustomException {

    public AuthorizationException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
