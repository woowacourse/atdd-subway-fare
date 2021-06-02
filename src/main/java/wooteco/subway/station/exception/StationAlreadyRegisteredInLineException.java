package wooteco.subway.station.exception;

public class StationAlreadyRegisteredInLineException extends
    RuntimeException {

    public StationAlreadyRegisteredInLineException(String message) {
        super(message);
    }
}
