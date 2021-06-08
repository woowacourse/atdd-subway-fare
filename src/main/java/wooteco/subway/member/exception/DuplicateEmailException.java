package wooteco.subway.member.exception;

public class DuplicateEmailException extends MemberDaoException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
