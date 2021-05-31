package wooteco.subway.infrastructure.exception.duplicate;

public class DuplicatedException extends RuntimeException {
    public DuplicatedException(String message) {
        super(message);
    }
}
