package wooteco.subway.station.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "지하철 역이 중복되었습니다.")
public class DuplicateStationException extends RuntimeException{
    public DuplicateStationException() {
    }
}
