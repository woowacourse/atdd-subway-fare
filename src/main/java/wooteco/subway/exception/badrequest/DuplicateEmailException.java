package wooteco.subway.exception.badrequest;

public class DuplicateEmailException extends BadRequest {
    private static final String MESSAGE = "이미 가입된 이메일입니다";

    public DuplicateEmailException() {
        super(MESSAGE);
    }

    public DuplicateEmailException(String message) {
        super(message);
    }

    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
