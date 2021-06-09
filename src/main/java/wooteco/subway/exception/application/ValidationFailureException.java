package wooteco.subway.exception.application;

import wooteco.subway.exception.web.BadRequestException;

public class ValidationFailureException extends BadRequestException {

    public ValidationFailureException(String message) {
        super(message);
    }
}
