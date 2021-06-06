package wooteco.subway.exception;

public class DuplicateEmailException extends DuplicateException {

    public DuplicateEmailException() {
        super("중복된 이메일입니다.");
    }
}
