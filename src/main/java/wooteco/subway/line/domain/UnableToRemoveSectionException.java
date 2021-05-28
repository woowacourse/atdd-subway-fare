package wooteco.subway.line.domain;

import wooteco.subway.exception.SubwayException;

public class UnableToRemoveSectionException extends SubwayException {
    private static final String MESSAGE = "구간을 더 이상 삭제할 수 없습니다.";

    public UnableToRemoveSectionException() {
        super(MESSAGE);
    }
}
