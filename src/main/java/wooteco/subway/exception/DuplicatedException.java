package wooteco.subway.exception;

public class DuplicatedException extends RuntimeException {
    private static final String DUPLICATED_MESSAGE_FORMAT = "%s(이)가 중복입니다.";

    public DuplicatedException() {
    }

    public DuplicatedException(String message) {
        super(String.format(DUPLICATED_MESSAGE_FORMAT, message));
    }
}
