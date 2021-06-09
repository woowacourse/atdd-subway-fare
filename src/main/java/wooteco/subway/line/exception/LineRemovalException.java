package wooteco.subway.line.exception;

import wooteco.subway.exception.web.BadRequestException;

public class LineRemovalException extends BadRequestException {

    public LineRemovalException(String message) {
        super(message);
    }
}
