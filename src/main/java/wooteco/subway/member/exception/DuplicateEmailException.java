package wooteco.subway.member.exception;

import wooteco.subway.config.exception.BadRequestException;

public class DuplicateEmailException extends BadRequestException {
    private static final String ERROR_MESSAGE = "이미 가입된 이메일 입니다.";

    public DuplicateEmailException() {
        super(ERROR_MESSAGE);
    }
}

