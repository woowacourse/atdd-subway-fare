package wooteco.subway.line.exception;

import wooteco.subway.exception.web.BadRequestException;

public class LineCompositionException extends BadRequestException {

    public LineCompositionException(String message) {
        super(message);
    }
}
