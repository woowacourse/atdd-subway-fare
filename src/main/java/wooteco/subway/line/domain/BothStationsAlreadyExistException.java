package wooteco.subway.line.domain;

import wooteco.subway.exception.SubwayException;

public class BothStationsAlreadyExistException extends SubwayException {
    private static final String MESSAGE = "상행역과 하행역 모두 이미 노선 상에 등록되어 있습니다.";

    public BothStationsAlreadyExistException() {
        super(MESSAGE);
    }
}
