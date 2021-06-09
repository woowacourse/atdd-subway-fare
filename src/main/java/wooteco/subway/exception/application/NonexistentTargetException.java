package wooteco.subway.exception.application;

import wooteco.subway.exception.web.BadRequestException;

public class NonexistentTargetException extends BadRequestException {

    public NonexistentTargetException(String description) {
        super(String.format("대상이 존재하지 않습니다. (%s)", description));
    }
}
