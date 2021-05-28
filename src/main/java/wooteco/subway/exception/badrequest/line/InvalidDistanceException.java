package wooteco.subway.exception.badrequest.line;

import wooteco.subway.exception.badrequest.BadRequestException;

public class InvalidDistanceException extends BadRequestException {
    private static final String MESSAGE = "잘못된 노선 구간 거리입니다.";

    public InvalidDistanceException() {
        super(MESSAGE);
    }
}
