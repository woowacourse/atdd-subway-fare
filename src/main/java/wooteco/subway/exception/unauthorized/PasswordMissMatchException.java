package wooteco.subway.exception.unauthorized;

public class PasswordMissMatchException extends UnAuthorized{
    private static final String MESSAGE = "입력하신 비밀번호가 일치하지 않습니다.";

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
