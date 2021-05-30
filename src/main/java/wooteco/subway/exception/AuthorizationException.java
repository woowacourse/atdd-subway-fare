package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends SubwayException {

    public AuthorizationException() {
        super("만료되었거나 유효하지 않은 토큰 정보입니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String error() {
        return "INVALID_TOKEN";
    }
}
