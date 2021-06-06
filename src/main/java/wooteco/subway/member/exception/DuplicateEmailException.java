package wooteco.subway.member.exception;

public class DuplicateEmailException extends RuntimeException {
    private static final String ERROR_MESSAGE = "이미 가입된 이메일입니다.";

    public DuplicateEmailException() {
        super(ERROR_MESSAGE);
    }
}
