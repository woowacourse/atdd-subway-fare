package wooteco.subway.exception;

public enum ErrorCode {
    // MEMBER
    DUPLICATE_EMAIL("[ERROR] 이미 존재하는 이메일입니다. 다른 이메일을 사용해주세요.", 404);

    private final String message;
    private final int status;

    ErrorCode(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
