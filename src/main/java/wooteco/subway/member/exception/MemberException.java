package wooteco.subway.member.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public enum MemberException implements SubwayException {

    DUPLICATED_EMAIL_EXCEPTION("중복된 이메일입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_EMAIL("유효하지 않은 이메일입니다.", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    MemberException(String message, int status) {
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
