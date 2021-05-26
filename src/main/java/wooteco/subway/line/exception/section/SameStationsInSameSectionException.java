package wooteco.subway.line.exception.section;

public class SameStationsInSameSectionException extends SectionException {
    private static final String MESSAGE = "SAME_STATIONS_IN_SAME_SECTION";

    public SameStationsInSameSectionException() {
        super(MESSAGE);
    }
}
