package wooteco.subway.member.exception;

public class DuplicatedIdException extends RuntimeException {
    private static final String MESSAGE = "DUPLICATED_ID";

    public DuplicatedIdException() {
        super(MESSAGE);
    }
}
