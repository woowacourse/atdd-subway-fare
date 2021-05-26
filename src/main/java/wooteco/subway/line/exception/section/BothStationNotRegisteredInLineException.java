package wooteco.subway.line.exception.section;

public class BothStationNotRegisteredInLineException extends SectionException {
    private static final String MESSAGE = "BOTH_STATION_NOT_REGISTERED_IN_LINE";

    public BothStationNotRegisteredInLineException() {
        super(MESSAGE);
    }
}
