package wooteco.subway.line.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "상행 종점역이 존재하지 않습니다.")
public class NotFoundStationOfSectionException extends RuntimeException {
    public NotFoundStationOfSectionException() {
    }
}
