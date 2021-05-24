package wooteco.subway.station.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "존재하지 않는 지하철 역입니다.")
public class NotFoundStationException extends RuntimeException {
    public NotFoundStationException() {
    }
}
