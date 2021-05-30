package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends SubwayException {

    public DuplicateEmailException() {
        super("중복된 아이디가 존재합니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "DUPLICATED_EMAIL";
    }
}
