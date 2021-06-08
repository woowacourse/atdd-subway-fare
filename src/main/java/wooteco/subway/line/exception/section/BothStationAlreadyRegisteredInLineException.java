package wooteco.subway.line.exception.section;

public class BothStationAlreadyRegisteredInLineException extends SectionException {
    private static final String MESSAGE = "BOTH_STATION_ALREADY_REGISTERED_TO_LINE";

    public BothStationAlreadyRegisteredInLineException() {
        super(MESSAGE);
    }
}
