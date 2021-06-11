package wooteco.subway.exception.member;

import wooteco.subway.exception.SubwayException;

public class InvalidLoginException extends SubwayException {
    public InvalidLoginException() {
        super("이메일 혹은 비밀번호를 다시 확인해주세요");
    }
}
