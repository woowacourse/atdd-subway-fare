package wooteco.subway.station.exception;

import wooteco.subway.exception.NotFoundException;

public final class NotFoundStationException extends NotFoundException {
    public NotFoundStationException() {
        super("존재하지 않는 지하철 역입니다.");
    }
}
