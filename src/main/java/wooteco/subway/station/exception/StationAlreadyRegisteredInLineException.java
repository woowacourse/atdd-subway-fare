package wooteco.subway.station.exception;

public class StationAlreadyRegisteredInLineException extends StationException {
    private static final String MESSAGE = "STATION_ALREADY_REGISTERED_IN_LINE";

    public StationAlreadyRegisteredInLineException() {
        super(MESSAGE);
    }
}
