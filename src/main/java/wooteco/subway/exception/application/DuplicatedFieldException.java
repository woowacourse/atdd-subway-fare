package wooteco.subway.exception.application;

import wooteco.subway.exception.web.BadRequestException;

public class DuplicatedFieldException extends BadRequestException {

    public DuplicatedFieldException(String description) {
        super(String.format("값이 중복되었습니다.(%s)", description));
    }
}
