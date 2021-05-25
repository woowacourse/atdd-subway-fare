package wooteco.subway.station.application;

import wooteco.subway.exception.BadRequestException;

public class DuplicateNameException extends BadRequestException {
    private static final String message = "중복된 이름입니다.";

    public DuplicateNameException() {
        super(message);
    }
}
