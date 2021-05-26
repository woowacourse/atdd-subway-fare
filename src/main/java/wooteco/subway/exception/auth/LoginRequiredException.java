package wooteco.subway.exception.auth;

public class LoginRequiredException extends AuthorizationException {
    private static final String MESSAGE = "로그인이 필요합니다.";

    public LoginRequiredException() {
        super(MESSAGE);
    }
}
