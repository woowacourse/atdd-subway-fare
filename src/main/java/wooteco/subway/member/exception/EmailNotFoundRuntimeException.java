package wooteco.subway.member.exception;

import wooteco.subway.exception.AuthorizationException;

public class EmailNotFoundRuntimeException extends AuthorizationException {
    private static final String ERROR_MESSAGE = "존재하지 않는 계정입니다";

    public EmailNotFoundRuntimeException() {
        super(ERROR_MESSAGE);
    }
}
