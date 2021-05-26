package wooteco.subway.line.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public enum LineException implements SubwayException {

    DUPLICATED_LINE_EXCEPTION("존재하는 노선 이름입니다.", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    LineException(String message, int status) {
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
