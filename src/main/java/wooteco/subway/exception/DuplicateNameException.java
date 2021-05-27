package wooteco.subway.exception;

public class DuplicateNameException extends IllegalArgumentException {
    public DuplicateNameException(String errorMessage) {
        super(errorMessage);
    }
}
