package wooteco.subway.exception.badrequest.duplication;

import wooteco.subway.exception.badrequest.BadRequestException;

public class DuplicatedException extends BadRequestException {

    public DuplicatedException(String message) {
        super(message);
    }
}
