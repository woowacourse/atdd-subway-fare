package wooteco.subway.exception.web;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class UnauthorizedException extends SubwayException {

    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
