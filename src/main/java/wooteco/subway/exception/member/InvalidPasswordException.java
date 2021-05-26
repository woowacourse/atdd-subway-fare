package wooteco.subway.exception.member;

import wooteco.subway.exception.SubwayException;

public class InvalidPasswordException extends SubwayException {
    public InvalidPasswordException() {
        super("현재 비밀번호를 다시 확인해주세요");
    }
}
