package wooteco.subway.exception.badrequest;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class BadRequest extends SubwayException {
    private static final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public BadRequest(String message) {
        super(message, httpStatus);
    }

    public BadRequest(String message, Throwable cause) {
        super(message, cause, httpStatus);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
