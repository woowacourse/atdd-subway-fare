package wooteco.subway.station.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "구간에 존재하는 지하철 역을 삭제할 수 없습니다.")
public class ExistStationInSectionException extends RuntimeException {
    public ExistStationInSectionException() {
    }
}
