package wooteco.subway.exception.notfound;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class NotFoundException extends SubwayException {

    public NotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
