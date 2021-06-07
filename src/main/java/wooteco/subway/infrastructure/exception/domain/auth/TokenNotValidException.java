package wooteco.subway.infrastructure.exception.domain.auth;

import wooteco.subway.infrastructure.ErrorCode;
import wooteco.subway.infrastructure.exception.domain.SubwayException;

public class TokenNotValidException extends SubwayException {
    public TokenNotValidException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
