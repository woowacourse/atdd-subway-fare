package wooteco.subway.member.exception;

public class NoSuchMemberException extends MemberException {
    private static final String MESSAGE = "NO_SUCH_MEMBER";

    public NoSuchMemberException() {
        super(MESSAGE);
    }
}
