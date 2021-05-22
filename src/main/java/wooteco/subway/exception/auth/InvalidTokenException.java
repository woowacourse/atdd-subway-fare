package wooteco.subway.exception.auth;

import wooteco.subway.exception.SubwayException;

public class InvalidTokenException extends SubwayException {
    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다.");
    }
}
