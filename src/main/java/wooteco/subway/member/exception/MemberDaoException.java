package wooteco.subway.member.exception;

import org.springframework.dao.DataAccessException;

public abstract class MemberDaoException extends DataAccessException {
    public MemberDaoException(String msg) {
        super(msg);
    }
}
