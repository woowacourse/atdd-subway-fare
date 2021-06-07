package wooteco.subway.station.exception;

public abstract class StationDomainException extends RuntimeException {
    public StationDomainException(String message) {
        super(message);
    }
}
