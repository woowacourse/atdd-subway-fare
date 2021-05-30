package wooteco.subway.line.exception;

public class BothStationNotRegisteredInLineException extends RuntimeException {

    public BothStationNotRegisteredInLineException(String message) {
        super(message);
    }
}
