package wooteco.subway.path.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public enum PathException implements SubwayException {

    INVALID_AGE_EXCEPTION("유효하지 않은 나이입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_DISTANCE_EXCEPTION("유효하지 않은 거리입니다.", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    PathException(String message, int status) {
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
