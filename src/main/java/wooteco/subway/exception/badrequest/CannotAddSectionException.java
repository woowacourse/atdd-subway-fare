package wooteco.subway.exception.badrequest;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class CannotAddSectionException extends SubwayException {

    public CannotAddSectionException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
