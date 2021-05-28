package wooteco.subway.member.application;

import wooteco.subway.exception.SubwayAuthorizationException;

public class InvalidPasswordRuntimeException extends SubwayAuthorizationException {
    private static final String errorMessage = "잘못된 비밀번호 입니다";

    public InvalidPasswordRuntimeException() {
        super(errorMessage);
    }
}
