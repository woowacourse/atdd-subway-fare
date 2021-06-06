package wooteco.subway.infrastructure.exception.not_found;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "존재하지 않는 지하철 역 입니다.")
public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException() {
    }
}
