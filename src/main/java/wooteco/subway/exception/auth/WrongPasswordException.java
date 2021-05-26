package wooteco.subway.exception.auth;

public class WrongPasswordException extends AuthorizationException {
    private static final String MESSAGE = "비밀번호가 틀렸습니다.";

    public WrongPasswordException() {
        super(MESSAGE);
    }
}
