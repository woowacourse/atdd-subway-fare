package wooteco.subway.exception.notfound;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class NotFound extends SubwayException {
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public NotFound(String message) {
        super(message, httpStatus);
    }

    public NotFound(String message, Throwable cause) {
        super(message, cause, httpStatus);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
