package wooteco.subway.exception.badrequest;

public class PasswordMissMatchException extends BadRequest {
    private static final String MESSAGE = "현재 비밀번호를 다시 확인해주세요";

    public PasswordMissMatchException() {
        super(MESSAGE);
    }

    public PasswordMissMatchException(String message) {
        super(message);
    }

    public PasswordMissMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
