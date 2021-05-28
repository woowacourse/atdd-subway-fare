package wooteco.subway.exception;

public class DuplicateException extends IllegalArgumentException {
    public DuplicateException(String errorMessage) {
        super(errorMessage);
    }
}
