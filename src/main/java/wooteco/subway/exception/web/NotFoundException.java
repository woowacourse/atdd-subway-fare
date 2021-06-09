package wooteco.subway.exception.web;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class NotFoundException extends SubwayException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
