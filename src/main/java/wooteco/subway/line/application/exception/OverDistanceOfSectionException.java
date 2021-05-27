package wooteco.subway.line.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "추가할 구간의 길이가 기존 구간의 길이보다 클 수 없습니다.")
public class OverDistanceOfSectionException extends RuntimeException {
    public OverDistanceOfSectionException() {
    }
}
