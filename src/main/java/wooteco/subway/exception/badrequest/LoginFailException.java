package wooteco.subway.exception.badrequest;

public class LoginFailException extends BadRequest {
    private static final String MESSAGE = "이메일 혹은 비밀번호를 다시 확인해주세요";

    public LoginFailException() {
        super(MESSAGE);
    }

    public LoginFailException(String message) {
        super(message);
    }

    public LoginFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
