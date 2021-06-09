package wooteco.subway.exception.application;

import wooteco.subway.exception.web.NotFoundException;

public class DataNotFoundException extends NotFoundException {

    public DataNotFoundException() {
        super("존재하지 않는 데이터입니다.");
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}
