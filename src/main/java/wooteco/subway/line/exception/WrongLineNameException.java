package wooteco.subway.line.exception;

import wooteco.subway.exception.WrongNameConventionException;

public class WrongLineNameException extends WrongNameConventionException {

    public WrongLineNameException(String name) {
        super(String.format("해당 역 이름은 잘못된 이름입니다. (%s)", name));
    }
}
