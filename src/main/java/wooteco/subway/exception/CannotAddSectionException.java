package wooteco.subway.exception;

public class CannotAddSectionException extends RuntimeException {

    public CannotAddSectionException() {
    }

    public CannotAddSectionException(String message) {
        super(message);
    }
}
