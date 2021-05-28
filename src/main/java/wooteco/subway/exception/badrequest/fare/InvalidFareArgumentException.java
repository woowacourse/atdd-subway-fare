package wooteco.subway.exception.badrequest.fare;

import wooteco.subway.exception.badrequest.BadRequestException;

public class InvalidFareArgumentException extends BadRequestException {
    private static final String MESSAGE = "요금 계산시 사용할 거리 혹은 나이 값이 잘못되었습니다.";

    public InvalidFareArgumentException() {
        super(MESSAGE);
    }
}
