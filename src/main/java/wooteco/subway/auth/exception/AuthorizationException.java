package wooteco.subway.auth.exception;

import wooteco.subway.exception.UnauthorizedException;

public final class AuthorizationException extends UnauthorizedException {
    public AuthorizationException() {
        super("이메일과 비밀번호를 확인해주세요.");
    }
}
