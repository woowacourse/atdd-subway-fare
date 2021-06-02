package wooteco.subway.exception.web;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class ConflictException extends SubwayException {

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
