package wooteco.subway.exception.station;

import wooteco.subway.exception.SubwayException;

public class DuplicateStationException extends SubwayException {
    public DuplicateStationException() {
        super("이미 존재하는 지하철 역입니다");
    }
}
