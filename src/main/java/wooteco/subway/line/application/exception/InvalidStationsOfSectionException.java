package wooteco.subway.line.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "구간에 대한 지하철 역 입력이 잘못되었습니다.")
public class InvalidStationsOfSectionException extends RuntimeException {
    public InvalidStationsOfSectionException() {
    }
}
