package wooteco.subway.member.exception;

public class MismatchIdPasswordException extends MemberException {
    private static final String MESSAGE = "MISMATCH_ID_PASSWORD";

    public MismatchIdPasswordException() {
        super(MESSAGE);
    }
}
