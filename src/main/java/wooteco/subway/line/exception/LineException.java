package wooteco.subway.line.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public enum LineException implements SubwayException {

    DUPLICATED_LINE_NAME_EXCEPTION("존재하는 노선 이름입니다.", HttpStatus.BAD_REQUEST.value()),
    DUPLICATED_LINE_COLOR_EXCEPTION("존재하는 노선 색상입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_LINE_EXCEPTION_EXCEPTION("잘못된 노선 이름입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_LINE_COLOR_EXCEPTION("잘못된 색깔입니다.", HttpStatus.BAD_REQUEST.value()),
    NOT_EXIST_LINE_EXCEPTION("존재하지 않는 노선입니다.", HttpStatus.NOT_FOUND.value());

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
