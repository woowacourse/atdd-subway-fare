package wooteco.subway.line.exception;

public class BothStationAlreadyRegisteredInLineException extends RuntimeException {

    public BothStationAlreadyRegisteredInLineException(String message) {
        super(message);
    }
}
