package wooteco.subway.member.domain;

import wooteco.subway.exception.AuthorizationException;

public class WrongPasswordException extends AuthorizationException {
    private static final String MESSAGE = "비밀번호가 정확하지 않습니다.";

    public WrongPasswordException() {
        super(MESSAGE);
    }
}
