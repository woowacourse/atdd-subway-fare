package wooteco.subway.member.exception;

import wooteco.subway.exception.SubwayException;

public class DuplicateEmailException extends SubwayException {
    private static final String errorMessage = "이미 가입된 이메일 입니다.";

    public DuplicateEmailException() {
        super(errorMessage);
    }
}

