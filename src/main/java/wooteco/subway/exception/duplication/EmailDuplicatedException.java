package wooteco.subway.exception.duplication;

public class EmailDuplicatedException extends DuplicatedException {
    private static final String MESSAGE = "중복된 이메일입니다.";

    public EmailDuplicatedException() {
        super(MESSAGE);
    }
}
