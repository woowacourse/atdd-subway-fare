package wooteco.subway.member.exception;

import wooteco.subway.exception.web.ConflictException;

public class DuplicatedEmailAddressException extends ConflictException {

    public DuplicatedEmailAddressException(String email) {
        super(String.format("이미 존재하는 email 입니다.(%s)", email));
    }
}
