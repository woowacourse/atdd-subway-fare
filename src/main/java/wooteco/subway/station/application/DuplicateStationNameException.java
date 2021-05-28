package wooteco.subway.station.application;

import wooteco.subway.exception.SubwayException;

public class DuplicateStationNameException extends SubwayException {
    private static final String MESSAGE = "중복된 역 이름입니다.";

    public DuplicateStationNameException() {
        super(MESSAGE);
    }
}
