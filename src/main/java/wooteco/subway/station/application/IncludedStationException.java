package wooteco.subway.station.application;

import wooteco.subway.aop.exception.SubwayException;

public class IncludedStationException extends SubwayException {

    public IncludedStationException() {
        super("구간에 추가되어있는 역은 삭제할 수 없습니다람쥐.", "Included");
    }
}
