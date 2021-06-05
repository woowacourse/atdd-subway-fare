package wooteco.subway.member.exception;

import wooteco.subway.exception.UnprocessableEntityException;

public final class DuplicateEmailException extends UnprocessableEntityException {
    public DuplicateEmailException() {
        super("이메일이 중복되었습니다.");
    }
}
