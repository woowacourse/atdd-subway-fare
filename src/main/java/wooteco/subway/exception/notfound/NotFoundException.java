package wooteco.subway.exception.notfound;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class NotFoundException extends SubwayException {
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public NotFoundException(String message) {
        super(message, httpStatus);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause, httpStatus);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
