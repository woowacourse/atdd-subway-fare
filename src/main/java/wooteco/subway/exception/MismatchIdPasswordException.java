package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class MismatchIdPasswordException extends SubwayException {

    public MismatchIdPasswordException() {
        super("아이디 또는 패스워드가 일치하지 않습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "MISMATCH_ID_PASSWORD";
    }
}
