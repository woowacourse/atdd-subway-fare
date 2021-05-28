package wooteco.subway.exception.badrequest.addition;

import wooteco.subway.exception.badrequest.BadRequestException;

public class CannotAddException extends BadRequestException {

    public CannotAddException(String message) {
        super(message);
    }
}
