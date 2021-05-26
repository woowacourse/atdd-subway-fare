package wooteco.subway.exception.unauthorized;

public class AuthorizationException extends UnauthorizedException {

    private static final String MESSAGE = "이메일 혹은 비밀번호를 다시 확인해주세요.";

    public AuthorizationException() {
        super(MESSAGE);
    }
}
