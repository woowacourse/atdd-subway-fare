package wooteco.subway.station.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StationException extends RuntimeException {
    public StationException(String message) {
        super(message);
    }
}