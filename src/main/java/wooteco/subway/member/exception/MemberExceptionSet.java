package wooteco.subway.member.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayExceptionSetInterface;

public enum MemberExceptionSet implements SubwayExceptionSetInterface {

    DUPLICATE_EMAIL_EXCEPTION("중복된 이메일 입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_EMAIL_EXCEPTION("잘못된 이메일 입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_PASSWORD_EXCEPTION("잘못된 비밀번호 입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_AGE_EXCEPTION("잘못된 나이입니다.", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    MemberExceptionSet(String message, int status) {
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
