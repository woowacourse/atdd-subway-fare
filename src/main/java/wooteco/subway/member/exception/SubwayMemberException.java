package wooteco.subway.member.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public enum SubwayMemberException implements SubwayException {

    DUPLICATE_EMAIL_EXCEPTION("중복된 이메일 입니다.", HttpStatus.BAD_REQUEST.value());

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
