package wooteco.subway.exception.notfound;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.exception.dto.ErrorResponse;

public abstract class NotFoundException extends SubwayException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, new ErrorResponse(message));
    }
}
