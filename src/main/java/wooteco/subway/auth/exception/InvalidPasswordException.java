package wooteco.subway.auth.exception;

import wooteco.subway.config.exception.AuthorizationException;

public class InvalidPasswordException extends AuthorizationException {
    public static final String ERROR_MESSAGE = "잘못된 비밀번호 입니다";

    public InvalidPasswordException() {
        super(ERROR_MESSAGE);
    }
}
