package wooteco.subway.exception.unprocessableentity;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.exception.dto.ErrorResponse;

public abstract class UnprocessableEntityException extends SubwayException {
    public UnprocessableEntityException(String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, new ErrorResponse(message));
    }
}
