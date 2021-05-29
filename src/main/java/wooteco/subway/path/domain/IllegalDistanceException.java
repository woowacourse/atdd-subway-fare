package wooteco.subway.path.domain;

import wooteco.subway.exception.SubwayException;

public class IllegalDistanceException extends SubwayException {
    private static final String MESSAGE = "유효하지 않은 거리 입니다.";

    public IllegalDistanceException() {
        super(MESSAGE);
    }
}
