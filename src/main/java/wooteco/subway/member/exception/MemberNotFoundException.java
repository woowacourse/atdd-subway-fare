package wooteco.subway.member.exception;

import wooteco.subway.exception.web.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(String email) {
        super(String.format("유저를 찾지 못했습니다.(email: %s)", email));
    }
}
