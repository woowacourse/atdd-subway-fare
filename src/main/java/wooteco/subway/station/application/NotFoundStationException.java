package wooteco.subway.station.application;

import wooteco.subway.SubwayException;

public class NotFoundStationException extends SubwayException {
    public NotFoundStationException() {
        super("역이 존재하지 않습니다.", "Empty");
    }
}
