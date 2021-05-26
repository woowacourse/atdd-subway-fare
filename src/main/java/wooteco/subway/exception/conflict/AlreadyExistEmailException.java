package wooteco.subway.exception.conflict;

public class AlreadyExistEmailException extends ConflictException {

    private static final String MESSAGE = "이미 사용된 이메일입니다.";

    public AlreadyExistEmailException() {
        super(MESSAGE);
    }
}