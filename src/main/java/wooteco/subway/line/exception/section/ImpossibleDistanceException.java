package wooteco.subway.line.exception.section;

public class ImpossibleDistanceException extends SectionException {
    private static final String MESSAGE = "IMPOSSIBLE_DISTANCE";

    public ImpossibleDistanceException() {
        super(MESSAGE);
    }
}
