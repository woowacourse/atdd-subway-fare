package wooteco.subway.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateEmailException extends DuplicateKeyException {

    public DuplicateEmailException() {
        super("중복된 이메일입니다.");
    }
}
