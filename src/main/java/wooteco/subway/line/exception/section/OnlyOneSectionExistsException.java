package wooteco.subway.line.exception.section;

public class OnlyOneSectionExistsException extends SectionException {
    private static final String MESSAGE = "ONLY_ONE_SECTION_EXISTS";

    public OnlyOneSectionExistsException() {
        super(MESSAGE);
    }
}
