package wooteco.subway.exception.member;

import wooteco.subway.exception.NotFoundException;

public class NotFoundMemberException extends NotFoundException {
    public NotFoundMemberException() {
        super("존재하지 않는 회원입니다");
    }
}
