package wooteco.subway.station.exception;

public class InvalidStationIdException extends StationDaoException {
    public InvalidStationIdException(String message) {
        super(message);
    }
}
