package wooteco.subway.line.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public enum SectionException implements SubwayException {

    INVALID_SECTION_DISTANCE_EXCEPTION("잘못된 노선 구간 거리입니다.", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    SectionException(String message, int status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public int status() {
        return status;
    }
}
