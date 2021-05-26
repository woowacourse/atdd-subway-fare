package wooteco.subway.line.exception.section;

public class NoSuchSectionException extends SectionException {
    private static final String MESSAGE = "NO_SUCH_SECTION";

    public NoSuchSectionException() {
        super(MESSAGE);
    }
}
