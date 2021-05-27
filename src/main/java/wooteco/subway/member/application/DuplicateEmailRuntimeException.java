package wooteco.subway.member.application;

import wooteco.subway.exception.SubwayRuntimeException;

public class DuplicateEmailRuntimeException extends SubwayRuntimeException {
    private static final String errorMessage = "이미 가입된 이메일 입니다.";

    public DuplicateEmailRuntimeException() {
        super(errorMessage);
    }
}

