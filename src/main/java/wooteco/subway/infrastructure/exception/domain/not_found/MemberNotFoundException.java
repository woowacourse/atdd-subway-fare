package wooteco.subway.infrastructure.exception.domain.not_found;

import wooteco.subway.infrastructure.ErrorCode;
import wooteco.subway.infrastructure.exception.domain.SubwayException;

public class MemberNotFoundException extends SubwayException {

    public MemberNotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
