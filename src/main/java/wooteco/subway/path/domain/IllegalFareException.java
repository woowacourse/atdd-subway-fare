package wooteco.subway.path.domain;

import wooteco.subway.exception.SubwayException;

public class IllegalFareException extends SubwayException {
    private static final String MESSAGE = "요금은 음수일 수 없습니다.";

    public IllegalFareException() {
        super(MESSAGE);
    }
}
