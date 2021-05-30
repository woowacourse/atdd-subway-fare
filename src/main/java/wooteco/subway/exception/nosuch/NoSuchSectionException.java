package wooteco.subway.exception.nosuch;

import wooteco.subway.exception.SubwayException;

public class NoSuchSectionException extends SubwayException {
    private static final String ERROR = "NO_SUCH_SECTION";
    private static final String MESSAGE = "구간이 존재하지 않습니다.";

    public NoSuchSectionException() {
        super(ERROR, MESSAGE);
    }
}
