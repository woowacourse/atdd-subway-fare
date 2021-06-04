package wooteco.subway.auth.exception;

import wooteco.subway.config.exception.AuthorizationException;

public class EmailNotFoundException extends AuthorizationException {
    public static final String ERROR_MESSAGE = "존재하지 않는 계정입니다";

    public EmailNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
