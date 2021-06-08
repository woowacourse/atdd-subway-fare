package wooteco.subway.station.exception;

public class NoSuchStationException extends StationException {
    private static final String MESSAGE = "NO_SUCH_STATION";

    public NoSuchStationException() {
        super(MESSAGE);
    }
}
