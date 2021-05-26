package wooteco.subway.exception.unauthorized;

public class NotLoginException extends UnauthorizedException {

    private static final String MESSAGE = "로그인을 해야 합니다.";

    public NotLoginException() {
        super(MESSAGE);
    }
}
