package wooteco.subway.exception.deletion;

public class CannotDeleteException extends RuntimeException {

    public CannotDeleteException(String message) {
        super(message);
    }
}
