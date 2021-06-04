package wooteco.subway.member.exception;

import wooteco.subway.exception.NotPermittedException;

public class MemberModifyNotAllowedException extends NotPermittedException {
    public MemberModifyNotAllowedException() {
    }

    public MemberModifyNotAllowedException(String message) {
        super(message);
    }
}
