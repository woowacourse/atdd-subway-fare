package wooteco.subway.exception;

public class DuplicateColorException extends IllegalArgumentException {
    public DuplicateColorException(String errorMessage) {
        super(errorMessage);
    }
}
