package wooteco.subway.member.exception;

import wooteco.subway.exception.BusinessException;
import wooteco.subway.exception.ErrorCode;

public class SamePasswordException extends BusinessException {

    public SamePasswordException() {
        super(ErrorCode.SAME_PASSWORD);
    }
}
