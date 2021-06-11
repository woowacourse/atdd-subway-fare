package wooteco.subway.exception.member;

import wooteco.subway.exception.SubwayException;

public class SamePasswordException extends SubwayException {
    public SamePasswordException() {
        super("현재 사용 중인 비밀번호입니다. 다른 비밀번호를 입력해주세요");
    }
}
