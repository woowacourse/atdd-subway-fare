package wooteco.subway.exception.duplicate;

public class EmailDuplicatedException extends DuplicatedException {
    private static final String MESSAGE = "중복된 이메일 입니다.";

    public EmailDuplicatedException() {
        super(MESSAGE);
    }
}
