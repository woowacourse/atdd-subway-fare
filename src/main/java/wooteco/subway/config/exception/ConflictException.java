package wooteco.subway.config.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends HttpException {

    private static final HttpStatus DEFAULT_STATUS = HttpStatus.CONFLICT;

    public ConflictException() {
        super(DEFAULT_STATUS);
    }

    public ConflictException(String message) {
        super(message, DEFAULT_STATUS);
    }
}

