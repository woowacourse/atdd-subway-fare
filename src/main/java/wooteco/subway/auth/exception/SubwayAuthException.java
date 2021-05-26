package wooteco.subway.auth.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public enum SubwayAuthException implements SubwayException {

    INVALID_JWT_EXCEPTION("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED.value()),
    NOT_EXIST_TOKEN_EXCEPTION("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED.value()),
    NOT_EXIST_MEMBER_EXCEPTION("존재하지 않는 사용자입니다.", HttpStatus.UNAUTHORIZED.value()),
    NOT_EXIST_EMAIL_EXCEPTION("잘못된 이메일입니다.", HttpStatus.UNAUTHORIZED.value()),
    ILLEGAL_PASSWORD_EXCEPTION("비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED.value());

    private final String message;
    private final int status;

    SubwayAuthException(String message, int status) {
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
