package wooteco.subway.exception.web;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class BadRequestException extends SubwayException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
