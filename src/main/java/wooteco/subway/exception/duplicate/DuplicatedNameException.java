package wooteco.subway.exception.duplicate;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public abstract class DuplicatedNameException extends SubwayException {

    public DuplicatedNameException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
