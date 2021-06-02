package wooteco.subway.auth.application;

import wooteco.subway.exception.web.UnauthorizedException;

public class AuthorizationException extends UnauthorizedException {

    public AuthorizationException(String message) {
        super(String.format("인증에 실패했습니다. (%s)", message));
    }
}
