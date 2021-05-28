package wooteco.subway.exception.badrequest.deletion;

import wooteco.subway.exception.badrequest.BadRequestException;

public class CannotDeleteException extends BadRequestException {

    public CannotDeleteException(String message) {
        super(message);
    }
}
