package wooteco.subway.exception.auth;

public class WrongEmailException extends AuthorizationException {
    private static final String MESSAGE = "잘못된 이메일입니다.";

    public WrongEmailException() {
        super(MESSAGE);
    }
}
