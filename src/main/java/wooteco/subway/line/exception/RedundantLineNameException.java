package wooteco.subway.line.exception;

import wooteco.subway.exception.SubwayException;

public class RedundantLineNameException extends SubwayException {
    private static final String ERROR_MESSAGE = "이미 존재하는 노선 이름 입니다.";

    public RedundantLineNameException() {
        super(ERROR_MESSAGE);
    }
}
