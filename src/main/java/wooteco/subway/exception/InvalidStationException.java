package wooteco.subway.exception;

import org.springframework.dao.EmptyResultDataAccessException;

public class InvalidStationException extends EmptyResultDataAccessException {

    public InvalidStationException() {
        this("존재하지 않는 역입니다.", 1);
    }

    public InvalidStationException(String msg, int expectedSize) {
        super(msg, expectedSize);
    }
}
