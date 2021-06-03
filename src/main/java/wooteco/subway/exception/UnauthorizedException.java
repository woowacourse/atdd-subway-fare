package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.dto.ErrorResponse;

public abstract class UnauthorizedException extends SubwayException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, new ErrorResponse(message));
    }
}
