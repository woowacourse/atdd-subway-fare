package wooteco.subway.exception.badrequest;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.exception.dto.ErrorResponse;

public class BadRequestException extends SubwayException {
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, new ErrorResponse(message));
    }
}
