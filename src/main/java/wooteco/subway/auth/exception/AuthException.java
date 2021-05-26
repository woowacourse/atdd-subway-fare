package wooteco.subway.auth.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public enum AuthException implements SubwayException {

    INVALID_TOKEN_EXCEPTION("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED.value()),
    NOT_EXIST_EMAIL_EXCEPTION("잘못된 이메일입니다.", HttpStatus.UNAUTHORIZED.value()),
    WRONG_PASSWORD_EXCEPTION("비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED.value());

    private final String message;
    private final int status;

    AuthException(String message, int status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public int status() {
        return status;
    }
}
