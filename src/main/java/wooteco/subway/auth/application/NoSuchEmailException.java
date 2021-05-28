package wooteco.subway.auth.application;

import wooteco.subway.exception.AuthorizationException;

public class NoSuchEmailException extends AuthorizationException {
    private static final String MESSAGE = "존재하지 않는 이메일입니다.";

    public NoSuchEmailException() {
        super(MESSAGE);
    }
}
