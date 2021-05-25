package wooteco.subway.auth.application;

import wooteco.subway.exception.BadRequestException;

public class AuthorizationException extends BadRequestException {
    private static final String message = "인증에 에러가 발생했습니다.";

    public AuthorizationException() {
        super(message);
    }
}
