package wooteco.subway.exception.unauthorized;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.exception.dto.ErrorResponse;

public class UnauthorizedException extends SubwayException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, new ErrorResponse(message));
    }
}
