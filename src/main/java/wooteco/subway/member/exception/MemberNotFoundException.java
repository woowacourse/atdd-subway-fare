package wooteco.subway.member.exception;

import wooteco.subway.exception.ErrorCode;
import wooteco.subway.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException() {
        super(ErrorCode.NOTFOUND_MEMBER);
    }
}
