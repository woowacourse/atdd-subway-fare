package wooteco.subway.infrastructure.exception.domain.auth;

import wooteco.subway.infrastructure.ErrorCode;
import wooteco.subway.infrastructure.exception.domain.SubwayException;

public class AuthorizationException extends SubwayException {

    public AuthorizationException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
