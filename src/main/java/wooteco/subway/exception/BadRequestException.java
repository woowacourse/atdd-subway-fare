package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.dto.ErrorResponse;

public abstract class BadRequestException extends SubwayException {
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, new ErrorResponse(message));
    }
}
