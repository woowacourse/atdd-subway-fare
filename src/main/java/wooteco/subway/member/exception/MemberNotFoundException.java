package wooteco.subway.member.exception;

import wooteco.subway.exception.DataNotFoundException;

public class MemberNotFoundException extends DataNotFoundException {

    public MemberNotFoundException(String email) {
        super(String.format("유저를 찾지 못했습니다.(email: %s)", email));
    }
}
