package wooteco.subway.exception.auth;

import wooteco.subway.exception.ExceptionStatus;

public enum AuthorizationExceptionStatus implements ExceptionStatus {
    WRONG_PASSWORD(401, "비밀번호가 틀렸습니다."),
    WRONG_EMAIL(401, "잘못된 이메일입니다."),
    LOGIN_REQUIRED(401, "로그인이 필요합니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다.");

    private final int status;
    private final String message;

    AuthorizationExceptionStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
