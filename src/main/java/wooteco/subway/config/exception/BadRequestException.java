package wooteco.subway.config.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpException {

    private static final HttpStatus DEFAULT_STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException() {
        super(DEFAULT_STATUS);
    }

    public BadRequestException(String message) {
        super(message, DEFAULT_STATUS);
    }
}
