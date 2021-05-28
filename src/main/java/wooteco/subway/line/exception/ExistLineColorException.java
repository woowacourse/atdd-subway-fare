package wooteco.subway.line.exception;

import wooteco.subway.exception.SubwayRuntimeException;

public class ExistLineColorException extends SubwayRuntimeException {
    private static final String ERROR_MESSAGE = "이미 존재하는 노선 색깔 입니다.";

    public ExistLineColorException() {
        super(ERROR_MESSAGE);
    }
}
