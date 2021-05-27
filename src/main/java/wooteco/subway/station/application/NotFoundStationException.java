package wooteco.subway.station.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundStationException extends RuntimeException {
    public NotFoundStationException() {
    }

    public NotFoundStationException(String message) {
        super(message);
    }
}
