package wooteco.subway.exception.unauthorized;

public class AuthorizationException extends UnauthorizedException {
    public AuthorizationException() {
        super("이메일과 비밀번호를 확인해주세요.");
    }
}
