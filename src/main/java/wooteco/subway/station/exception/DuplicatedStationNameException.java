package wooteco.subway.station.exception;

public class DuplicatedStationNameException extends StationException {
    private static final String MESSAGE = "DUPLICATED_STATION_NAME";

    public DuplicatedStationNameException() {
        super(MESSAGE);
    }
}
