package wooteco.subway.member.exception;

import wooteco.subway.exception.SubwayAuthorizationException;

public class EmailNotFoundRuntimeException extends SubwayAuthorizationException {
    private static final String errorMessage = "존재하지 않는 계정입니다";

    public EmailNotFoundRuntimeException() {
        super(errorMessage);
    }
}
