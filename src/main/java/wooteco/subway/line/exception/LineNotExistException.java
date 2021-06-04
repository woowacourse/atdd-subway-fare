package wooteco.subway.line.exception;

import wooteco.subway.config.exception.BadRequestException;

public class LineNotExistException extends BadRequestException {
    private static final String ERROR_MESSAGE = "조회하려는 노선이 없습니다.";

    public LineNotExistException() {
        super(ERROR_MESSAGE);
    }
}
