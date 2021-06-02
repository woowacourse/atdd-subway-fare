package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    private final HttpStatus statusCode;
    private final String body;

    public CustomException(HttpStatus statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public HttpStatus statusCode() {
        return this.statusCode;
    }

    public String body() {
        return this.body;
    }

}
