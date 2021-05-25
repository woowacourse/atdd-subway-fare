package wooteco.subway.auth.application;

import wooteco.subway.exception.SubwayException;

public class NoSuchEmailException extends SubwayException {
    private static final String message = "잘못된 이메일입니다.";

    public NoSuchEmailException() {
        super(message);
    }
}
