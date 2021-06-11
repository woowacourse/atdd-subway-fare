package wooteco.subway.exception.auth;

import wooteco.subway.exception.SubwayException;

public class InvalidTokenException extends SubwayException {
    public InvalidTokenException() {
        super("다시 로그인 후 시도해주세요");
    }
}
