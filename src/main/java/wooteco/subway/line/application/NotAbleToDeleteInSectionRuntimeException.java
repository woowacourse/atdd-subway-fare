package wooteco.subway.line.application;

import wooteco.subway.exception.SubwayRuntimeException;

public class NotAbleToDeleteInSectionRuntimeException extends SubwayRuntimeException {
    private static final String ERROR_MESSAGE = "구간이 하나인 경우 역 삭제 불가능합니다.";

    public NotAbleToDeleteInSectionRuntimeException() {
        super(ERROR_MESSAGE);
    }
}
