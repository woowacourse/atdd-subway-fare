package wooteco.subway.station.exception;

import org.springframework.dao.DataAccessException;

public abstract class StationDaoException extends DataAccessException {
    public StationDaoException(String msg) {
        super(msg);
    }
}
