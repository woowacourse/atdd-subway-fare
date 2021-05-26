package wooteco.subway.config.exception;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {

    public static final String ERROR_MESSAGE = "오류가 발생했습니다";

    private final HttpStatus status;

    public HttpException(HttpStatus status) {
        this(ERROR_MESSAGE, status);
    }

    public HttpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
