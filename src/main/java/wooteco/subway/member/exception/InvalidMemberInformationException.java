package wooteco.subway.member.exception;

public class InvalidMemberInformationException extends MemberDaoException {
    private static final String MESSAGE = "잘못된 정보를 기입하셨습니다.";

    public InvalidMemberInformationException() {
        super(MESSAGE);
    }
}
