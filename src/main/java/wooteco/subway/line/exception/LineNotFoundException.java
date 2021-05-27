package wooteco.subway.line.exception;

import wooteco.subway.exception.NotFoundException;

public class LineNotFoundException extends NotFoundException {
    public LineNotFoundException() {
    }

    public LineNotFoundException(String message) {
        super(message);
    }
}
