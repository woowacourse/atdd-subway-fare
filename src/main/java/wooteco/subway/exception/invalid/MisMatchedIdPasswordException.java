package wooteco.subway.exception.invalid;

import wooteco.subway.exception.SubwayException;

public class MisMatchedIdPasswordException extends SubwayException {
    private static final String ERROR = "MISMATCH_ID_PASSWORD";
    private static final String MESSAGE = "아이디와 비밀번호가 일치하지 않습니다.";

    public MisMatchedIdPasswordException() {
        super(ERROR, MESSAGE);
    }
}
