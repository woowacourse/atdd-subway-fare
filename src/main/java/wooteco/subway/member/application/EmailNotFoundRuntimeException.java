package wooteco.subway.member.application;

import wooteco.subway.exception.SubwayAuthorizationException;
import wooteco.subway.exception.SubwayRuntimeException;

public class EmailNotFoundRuntimeException extends SubwayAuthorizationException {
    private static final String errorMessage = "존재하지 않는 계정입니다";

    public EmailNotFoundRuntimeException() {
        super(errorMessage);
    }
}
