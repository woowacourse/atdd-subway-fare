package wooteco.subway.exception.nosuch;

import wooteco.subway.exception.SubwayException;

public class NoSuchStationException extends SubwayException {
    private static final String ERROR = "NO_SUCH_STATION";
    private static final String MESSAGE = "해당 역이 존재하지 않습니다.";

    public NoSuchStationException() {
        super(ERROR, MESSAGE);
    }
}
