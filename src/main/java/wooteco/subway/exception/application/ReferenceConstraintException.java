package wooteco.subway.exception.application;

import wooteco.subway.exception.web.BadRequestException;

public class ReferenceConstraintException extends BadRequestException {

    public ReferenceConstraintException(String message) {
        super(message);
    }
}
