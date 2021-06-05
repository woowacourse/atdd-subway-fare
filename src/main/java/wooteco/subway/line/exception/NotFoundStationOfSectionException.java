package wooteco.subway.line.exception;

import wooteco.subway.exception.NotFoundException;

public final class NotFoundStationOfSectionException extends NotFoundException {
    public NotFoundStationOfSectionException() {
        super("상행 종점역이 존재하지 않습니다.");
    }
}
