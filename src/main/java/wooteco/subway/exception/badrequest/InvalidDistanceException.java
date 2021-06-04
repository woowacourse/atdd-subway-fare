package wooteco.subway.exception.badrequest;

import org.springframework.http.HttpStatus;

public class InvalidDistanceException extends CannotAddSectionException {

    private static final String INVALID_DISTANCE = "기존 구간의 길이를 초과하여 등록할 수 없습니다.";

    public InvalidDistanceException() {
        super(HttpStatus.BAD_REQUEST, INVALID_DISTANCE);
    }
}
