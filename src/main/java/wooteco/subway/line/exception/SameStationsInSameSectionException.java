package wooteco.subway.line.exception;

public class SameStationsInSameSectionException extends RuntimeException {

    public SameStationsInSameSectionException(String message) {
        super(message);
    }
}
