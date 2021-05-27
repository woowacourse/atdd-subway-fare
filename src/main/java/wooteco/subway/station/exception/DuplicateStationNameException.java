package wooteco.subway.station.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateStationNameException extends DuplicateKeyException {
    private static final String ERROR_MESSAGE = "이미 등록된 역입니다.";

    public DuplicateStationNameException() {
        super(ERROR_MESSAGE);
    }
}
