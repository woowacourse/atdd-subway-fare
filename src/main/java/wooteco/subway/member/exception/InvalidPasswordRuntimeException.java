package wooteco.subway.member.exception;

import wooteco.subway.exception.AuthorizationException;

public class InvalidPasswordRuntimeException extends AuthorizationException {
    private static final String errorMessage = "잘못된 비밀번호 입니다";

    public InvalidPasswordRuntimeException() {
        super(errorMessage);
    }
}
