package wooteco.subway.member.application;

import wooteco.subway.aop.exception.SubwayException;

public class DuplicateMemberEmailException extends SubwayException {
    public DuplicateMemberEmailException() {
        super("중복된 이메일이 있습니다.", "Duplication");
    }
}
