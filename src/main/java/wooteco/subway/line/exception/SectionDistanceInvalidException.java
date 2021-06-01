package wooteco.subway.line.exception;

import wooteco.subway.config.exception.BadRequestException;

public class SectionDistanceInvalidException extends BadRequestException {
    private static final String ERROR_MESSAGE = "구간의 길이가 잘못되었습니다.";

    public SectionDistanceInvalidException() {
        super(ERROR_MESSAGE);
    }
}
