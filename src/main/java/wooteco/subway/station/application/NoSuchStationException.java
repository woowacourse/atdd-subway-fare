package wooteco.subway.station.application;

import wooteco.subway.exception.SubwayException;

public class NoSuchStationException extends SubwayException {
    private static final String MESSAGE = "존재하지 않는 역 입니다.";

    public NoSuchStationException() {
        super(MESSAGE);
    }
}
