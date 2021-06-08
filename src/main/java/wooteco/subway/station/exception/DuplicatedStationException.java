package wooteco.subway.station.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicatedStationException extends RuntimeException {

    public DuplicatedStationException(String name) {
        super(String.format("이미 존재하는 역 이름입니다. 이름: %s", name));
    }
}
