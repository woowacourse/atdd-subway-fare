package wooteco.subway.line.domain;

import wooteco.subway.exception.SubwayException;

public class BothStationsNotExistException extends SubwayException {
    private static final String MESSAGE = "추가될 상행역과 하행역 중 한 역은 반드시 노선 내에 존재해야 합니다.";

    public BothStationsNotExistException() {
        super(MESSAGE);
    }
}
