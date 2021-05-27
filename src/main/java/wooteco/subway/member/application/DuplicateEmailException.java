package wooteco.subway.member.application;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateEmailException extends DuplicateKeyException {
    private static final String MESSAGE = "중복된 이메일 입니다.";

    public DuplicateEmailException() {
        super(MESSAGE);
    }
}
