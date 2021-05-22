package wooteco.subway.exception.unauthorized;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class UnAuthorized extends SubwayException {
    private static final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public UnAuthorized(String message) {
        super(message, httpStatus);
    }

    public UnAuthorized(String message, Throwable cause) {
        super(message, cause, httpStatus);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
