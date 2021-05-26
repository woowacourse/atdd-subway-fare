package wooteco.subway.line.exception.section;

public class InvalidDistanceException extends SectionException{
    private static final String MESSAGE = "INVALID_DISTANCE";

    public InvalidDistanceException() {
        super(MESSAGE);
    }
}
