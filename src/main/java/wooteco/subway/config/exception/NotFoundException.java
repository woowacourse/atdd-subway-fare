package wooteco.subway.config.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException {

    private static final HttpStatus DEFAULT_STATUS = HttpStatus.NOT_FOUND;

    public NotFoundException() {
        super(DEFAULT_STATUS);
    }

    public NotFoundException(String message) {
        super(message, DEFAULT_STATUS);
    }
}
