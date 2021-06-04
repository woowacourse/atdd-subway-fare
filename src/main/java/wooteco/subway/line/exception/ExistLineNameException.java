package wooteco.subway.line.exception;

import wooteco.subway.config.exception.BadRequestException;

public class ExistLineNameException extends BadRequestException {
    private static final String ERROR_MESSAGE = "이미 존재하는 노선 이름 입니다.";

    public ExistLineNameException() {
        super(ERROR_MESSAGE);
    }
}
