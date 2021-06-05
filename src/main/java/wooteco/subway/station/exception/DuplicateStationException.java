package wooteco.subway.station.exception;

import wooteco.subway.exception.BadRequestException;

public final class DuplicateStationException extends BadRequestException {
    public DuplicateStationException() {
        super("이미 존재하는 지하철 역입니다.");
    }
}
