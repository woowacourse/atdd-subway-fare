package wooteco.subway.exception.badrequest;

public class UpdatePasswordException extends BadRequest {
    private static final String MESSAGE = "현재 사용 중인 비밀번호입니다. 다른 비밀번호를 입력해주세요";

    public UpdatePasswordException() {
        super(MESSAGE);
    }

    public UpdatePasswordException(String message) {
        super(message);
    }

    public UpdatePasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
