package wooteco.subway.line.exception;

import org.springframework.dao.DataAccessException;

public abstract class LineDaoException extends DataAccessException {
    public LineDaoException(String message) {
        super(message);
    }
}
