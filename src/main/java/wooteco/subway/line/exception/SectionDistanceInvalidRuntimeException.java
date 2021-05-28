package wooteco.subway.line.exception;

import wooteco.subway.exception.SubwayRuntimeException;

public class SectionDistanceInvalidRuntimeException extends SubwayRuntimeException {
    private static final String ERROR_MESSAGE = "구간의 길이가 잘못되었습니다.";

    public SectionDistanceInvalidRuntimeException() {
        super(ERROR_MESSAGE);
    }
}
