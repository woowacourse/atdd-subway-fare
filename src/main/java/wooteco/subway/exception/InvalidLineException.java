package wooteco.subway.exception;

import org.springframework.dao.EmptyResultDataAccessException;

public class InvalidLineException extends EmptyResultDataAccessException {

    public InvalidLineException() {
        this("존재하지 않는 노선입니다.", 1);
    }

    public InvalidLineException(String msg, int expectedSize) {
        super(msg, expectedSize);
    }
}
