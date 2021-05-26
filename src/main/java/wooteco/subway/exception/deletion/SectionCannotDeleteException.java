package wooteco.subway.exception.deletion;

public class SectionCannotDeleteException extends CannotDeleteException {
    private static final String MESSAGE = "노선에는 최소한 하나의 구간은 존재해야합니다.";

    public SectionCannotDeleteException() {
        super(MESSAGE);
    }
}
