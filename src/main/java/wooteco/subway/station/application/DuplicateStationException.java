package wooteco.subway.station.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "이미 존재하는 지하철 역입니다.")
public class DuplicateStationException extends RuntimeException{
    public DuplicateStationException() {
    }
}