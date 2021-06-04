package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class SubwayException extends RuntimeException {

    HttpStatus httpStatus;
    Object body;

    public SubwayException(HttpStatus httpStatus, Object body) {
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Object getBody() {
        return body;
    }
}
