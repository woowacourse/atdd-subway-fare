package wooteco.subway.exception.unauthorized;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class UnAuthorizedException extends SubwayException {
    private static final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public UnAuthorizedException(String message) {
        super(message, httpStatus);
    }

    public UnAuthorizedException(String message, Throwable cause) {
        super(message, cause, httpStatus);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
