package wooteco.subway.member.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public enum SubwayMemberException implements SubwayException {

    DUPLICATE_EMAIL_EXCEPTION("중복된 이메일 입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_EMAIL_EXCEPTION("유효하지 않은 이메일 입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_PASSWORD_EXCEPTION("유효하지 않은 비밀번호 입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_AGE_EXCEPTION("유효하지 않은 나이입니다.", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    SubwayMemberException(String message, int status) {
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
