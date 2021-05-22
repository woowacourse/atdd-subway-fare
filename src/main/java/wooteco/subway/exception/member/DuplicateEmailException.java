package wooteco.subway.exception.member;

import wooteco.subway.exception.SubwayException;

public class DuplicateEmailException extends SubwayException {
    public DuplicateEmailException() {
        super("이미 존재하는 이메일입니다.");
    }
}
